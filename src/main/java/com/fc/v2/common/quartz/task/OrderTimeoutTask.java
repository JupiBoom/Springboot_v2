package com.fc.v2.common.quartz.task;

import com.fc.v2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单超时处理任务
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 15:00
 */
@Component("orderTimeoutTask")
public class OrderTimeoutTask {

    @Autowired
    private OrderService orderService;

    /**
     * 处理超时未支付订单
     */
    public void handleTimeoutOrders() {
        System.out.println("正在执行超时未支付订单处理任务");
        orderService.handleTimeoutOrders();
    }
}