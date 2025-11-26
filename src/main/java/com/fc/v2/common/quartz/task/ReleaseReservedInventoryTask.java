package com.fc.v2.common.quartz.task;

import com.fc.v2.common.enumclass.OrderStatusEnum;
import com.fc.v2.mapper.auto.OrderRepository;
import com.fc.v2.model.auto.Order;
import com.fc.v2.model.auto.OrderItem;
import com.fc.v2.service.InventoryService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReleaseReservedInventoryTask extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseReservedInventoryTask.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        logger.info("开始执行库存预占超时释放任务");

        try {
            // 查询30分钟前创建的待支付订单
            Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30));
            List<Order> orders = orderRepository.findAll((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("status"), OrderStatusEnum.PENDING_PAYMENT.name()),
                            criteriaBuilder.lessThan(root.get("createTime"), thirtyMinutesAgo)
                    )
            );

            logger.info("找到需要释放库存的订单数量: {}", orders.size());

            // 释放每个订单的预占库存
            for (Order order : orders) {
                try {
                    for (OrderItem item : order.getOrderItems()) {
                        // 释放预占库存
                        boolean success = inventoryService.releaseReservedInventory(
                                item.getProductSku(), item.getWarehouseId(), item.getQuantity());
                        if (success) {
                            logger.info("订单 {} 商品 {} 预占库存释放成功", order.getOrderNo(), item.getProductSku());
                        } else {
                            logger.error("订单 {} 商品 {} 预占库存释放失败", order.getOrderNo(), item.getProductSku());
                        }
                    }

                    // 更新订单状态为已取消
                    order.setStatus(OrderStatusEnum.CANCELLED.name());
                    order.setCancelTime(new Date());
                    order.setUpdateTime(new Date());
                    orderRepository.save(order);
                    logger.info("订单 {} 状态更新为已取消", order.getOrderNo());
                } catch (Exception e) {
                    logger.error("处理订单 {} 时发生异常", order.getOrderNo(), e);
                }
            }

            logger.info("库存预占超时释放任务执行完成");
        } catch (Exception e) {
            logger.error("执行库存预占超时释放任务时发生异常", e);
        }
    }
}