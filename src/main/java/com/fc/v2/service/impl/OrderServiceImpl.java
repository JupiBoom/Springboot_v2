package com.fc.v2.service.impl;

import com.fc.v2.common.enumclass.OrderStatusEnum;
import com.fc.v2.mapper.auto.OrderRepository;
import com.fc.v2.model.auto.Order;
import com.fc.v2.model.auto.OrderLog;
import com.fc.v2.model.auto.OrderItem;
import com.fc.v2.service.OrderService;
import com.fc.v2.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.text.SimpleDateFormat;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StateMachineService<OrderStatusEnum, String> stateMachineService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // 生成订单号
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatusEnum.PENDING_PAYMENT.name());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 保存订单日志
        saveOrderLog(savedOrder, null, OrderStatusEnum.PENDING_PAYMENT.name(), "系统", "订单创建");

        return savedOrder;
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        // TODO: 实现根据用户ID查询订单列表
        return null;
    }

    @Override
    @Transactional
    public Order payOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 使用状态机处理状态变更
        StateMachine<OrderStatusEnum, String> stateMachine = stateMachineService.acquireStateMachine(orderNo);
        stateMachine.start();
        Message<String> message = MessageBuilder.withPayload("PAY").build();
        boolean result = stateMachine.sendEvent(message);
        stateMachineService.releaseStateMachine(orderNo);

        if (result) {
            order.setStatus(OrderStatusEnum.PAID.name());
            order.setPaymentTime(new Date());
            order.setUpdateTime(new Date());
            Order updatedOrder = orderRepository.save(order);

            // 保存订单日志
            saveOrderLog(updatedOrder, OrderStatusEnum.PENDING_PAYMENT.name(), OrderStatusEnum.PAID.name(), "系统", "订单支付");

            return updatedOrder;
        } else {
            throw new RuntimeException("订单状态变更失败");
        }
    }

    @Override
    @Transactional
    public Order shipOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 使用状态机处理状态变更
        StateMachine<OrderStatusEnum, String> stateMachine = stateMachineService.acquireStateMachine(orderNo);
        stateMachine.start();
        Message<String> message = MessageBuilder.withPayload("SHIP").build();
        boolean result = stateMachine.sendEvent(message);
        stateMachineService.releaseStateMachine(orderNo);

        if (result) {
            order.setStatus(OrderStatusEnum.SHIPPED.name());
            order.setShippingTime(new Date());
            order.setUpdateTime(new Date());
            Order updatedOrder = orderRepository.save(order);

            // 保存订单日志
            saveOrderLog(updatedOrder, OrderStatusEnum.PAID.name(), OrderStatusEnum.SHIPPED.name(), "系统", "订单发货");

            return updatedOrder;
        } else {
            throw new RuntimeException("订单状态变更失败");
        }
    }

    @Override
    @Transactional
    public Order confirmReceipt(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 使用状态机处理状态变更
        StateMachine<OrderStatusEnum, String> stateMachine = stateMachineService.acquireStateMachine(orderNo);
        stateMachine.start();
        Message<String> message = MessageBuilder.withPayload("CONFIRM_RECEIPT").build();
        boolean result = stateMachine.sendEvent(message);
        stateMachineService.releaseStateMachine(orderNo);

        if (result) {
            order.setStatus(OrderStatusEnum.COMPLETED.name());
            order.setFinishTime(new Date());
            order.setUpdateTime(new Date());
            Order updatedOrder = orderRepository.save(order);

            // 保存订单日志
            saveOrderLog(updatedOrder, OrderStatusEnum.SHIPPED.name(), OrderStatusEnum.COMPLETED.name(), "系统", "确认收货");

            return updatedOrder;
        } else {
            throw new RuntimeException("订单状态变更失败");
        }
    }

    @Override
    @Transactional
    public Order cancelOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 使用状态机处理状态变更
        StateMachine<OrderStatusEnum, String> stateMachine = stateMachineService.acquireStateMachine(orderNo);
        stateMachine.start();
        Message<String> message = MessageBuilder.withPayload("CANCEL").build();
        boolean result = stateMachine.sendEvent(message);
        stateMachineService.releaseStateMachine(orderNo);

        if (result) {
            order.setStatus(OrderStatusEnum.CANCELLED.name());
            order.setCancelTime(new Date());
            order.setUpdateTime(new Date());
            Order updatedOrder = orderRepository.save(order);

            // 保存订单日志
            saveOrderLog(updatedOrder, order.getStatus(), OrderStatusEnum.CANCELLED.name(), "系统", "订单取消");

            // 订单取消后回退库存
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                for (OrderItem item : order.getOrderItems()) {
                    // 回退库存
                    boolean rollbackResult = inventoryService.rollbackInventory(item.getProductSku(), item.getWarehouseId(), item.getQuantity());
                    if (!rollbackResult) {
                        throw new RuntimeException("库存回退失败: 商品SKU=" + item.getProductSku() + ", 仓库ID=" + item.getWarehouseId() + ", 数量=" + item.getQuantity());
                    }
                }
            }

            return updatedOrder;
        } else {
            throw new RuntimeException("订单状态变更失败");
        }
    }

    @Override
    public List<Order> splitOrder(Order order) {
        // TODO: 实现订单拆分逻辑
        return null;
    }

    @Override
    public Order mergeOrders(List<Order> orders) {
        // TODO: 实现订单合并逻辑
        return null;
    }

    @Override
    public Map<String, Object> statisticsOrderProcessingTime() {
        // 订单处理时效统计：计算从订单创建到支付、发货、确认收货的平均时间
        Map<String, Object> statistics = new HashMap<>();

        // 查询所有已完成的订单
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatusEnum.COMPLETED.name());

        if (completedOrders.isEmpty()) {
            statistics.put("averagePaymentTime", 0);
            statistics.put("averageShippingTime", 0);
            statistics.put("averageDeliveryTime", 0);
            return statistics;
        }

        long totalPaymentTime = 0;
        long totalShippingTime = 0;
        long totalDeliveryTime = 0;
        int paymentCount = 0;
        int shippingCount = 0;
        int deliveryCount = 0;

        for (Order order : completedOrders) {
            // 计算订单创建到支付的时间
            if (order.getPaymentTime() != null) {
                long paymentTime = order.getPaymentTime().getTime() - order.getCreateTime().getTime();
                totalPaymentTime += paymentTime;
                paymentCount++;
            }

            // 计算支付到发货的时间
            if (order.getPaymentTime() != null && order.getShippingTime() != null) {
                long shippingTime = order.getShippingTime().getTime() - order.getPaymentTime().getTime();
                totalShippingTime += shippingTime;
                shippingCount++;
            }

            // 计算发货到确认收货的时间
            if (order.getShippingTime() != null && order.getFinishTime() != null) {
                long deliveryTime = order.getFinishTime().getTime() - order.getShippingTime().getTime();
                totalDeliveryTime += deliveryTime;
                deliveryCount++;
            }
        }

        // 计算平均时间（单位：分钟）
        double averagePaymentTime = paymentCount > 0 ? (totalPaymentTime / (1000 * 60.0)) / paymentCount : 0;
        double averageShippingTime = shippingCount > 0 ? (totalShippingTime / (1000 * 60.0)) / shippingCount : 0;
        double averageDeliveryTime = deliveryCount > 0 ? (totalDeliveryTime / (1000 * 60.0)) / deliveryCount : 0;

        statistics.put("averagePaymentTime", averagePaymentTime);
        statistics.put("averageShippingTime", averageShippingTime);
        statistics.put("averageDeliveryTime", averageDeliveryTime);

        return statistics;
    }

    @Override
    public Map<String, Object> statisticsLogisticsTime() {
        // TODO: 实现物流时效统计
        return null;
    }

    @Override
    public Map<String, Object> analyzeExceptionOrders() {
        // TODO: 实现异常订单分析
        return null;
    }

    /**
     * 生成订单号
     * @return 订单号
     */
    private String generateOrderNo() {
        // 订单号格式：YYYYMMDDHHMMSS + 6位随机数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datePart = sdf.format(new Date());
        String randomPart = String.format("%06d", new Random().nextInt(1000000));
        return datePart + randomPart;
    }

    /**
     * 保存订单日志
     * @param order 订单
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     * @param operator 操作人
     * @param remark 备注
     */
    private void saveOrderLog(Order order, String oldStatus, String newStatus, String operator, String remark) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getId());
        orderLog.setOldStatus(oldStatus);
        orderLog.setNewStatus(newStatus);
        orderLog.setOperator(operator);
        orderLog.setOperateTime(new Date());
        orderLog.setRemark(remark);

        // 保存订单日志
        if (order.getOrderLogs() == null) {
            order.setOrderLogs(new ArrayList<>());
        }
        order.getOrderLogs().add(orderLog);
    }
}