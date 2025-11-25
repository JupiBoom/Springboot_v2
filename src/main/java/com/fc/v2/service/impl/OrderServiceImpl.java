package com.fc.v2.service.impl;

import com.fc.v2.mapper.custom.OrderRepository;
import com.fc.v2.mapper.custom.OrderStatusLogRepository;
import com.fc.v2.model.custom.Order;
import com.fc.v2.model.custom.OrderEvent;
import com.fc.v2.model.custom.OrderStatus;
import com.fc.v2.model.custom.OrderStatusLog;
import com.fc.v2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单Service实现类
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 14:00
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusLogRepository orderStatusLogRepository;

    @Autowired
    private StateMachineService<OrderStatus, OrderEvent> stateMachineService;

    /**
     * 创建订单
     * @param order 订单信息
     * @return 订单信息
     */
    @Override
    public Order createOrder(Order order) {
        // 生成唯一订单号
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        // 保存订单
        Order savedOrder = orderRepository.save(order);
        // 记录订单状态变更日志
        saveOrderStatusLog(savedOrder, OrderStatus.PENDING_PAYMENT, "创建订单", "system");
        return savedOrder;
    }

    /**
     * 根据订单ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    @Override
    public Order findOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * 根据订单状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    @Override
    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * 处理订单状态变更
     * @param orderNo 订单号
     * @param event 订单事件
     * @param reason 变更原因
     * @param operator 操作人
     * @return 是否处理成功
     */
    @Override
    public boolean handleOrderEvent(String orderNo, OrderEvent event, String reason, String operator) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return false;
        }

        // 获取或创建状态机实例
        StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineService.acquireStateMachine(orderNo);
        stateMachine.getExtendedState().getVariables().put("order", order);

        // 发送事件
        Message<OrderEvent> message = MessageBuilder.withPayload(event).build();
        boolean result = stateMachine.sendEvent(message);

        if (result) {
            // 更新订单状态
            OrderStatus newStatus = stateMachine.getState().getId();
            order.setStatus(newStatus);
            orderRepository.save(order);
            // 记录订单状态变更日志
            saveOrderStatusLog(order, order.getStatus(), reason, operator);
        }

        // 释放状态机实例
        stateMachineService.releaseStateMachine(orderNo);
        return result;
    }

    /**
     * 拆分订单
     * @param order 原始订单
     * @return 拆分后的订单列表
     */
    @Override
    public List<Order> splitOrder(Order order) {
        List<Order> splitOrders = new ArrayList<>();
        // TODO: 实现订单拆分逻辑，根据不同仓库/供应商拆分订单
        // 暂时返回原订单
        splitOrders.add(order);
        return splitOrders;
    }

    /**
     * 合并订单
     * @param orders 订单列表
     * @return 合并后的订单
     */
    @Override
    public Order mergeOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return null;
        }
        // TODO: 实现订单合并逻辑，同一用户的多订单合并
        // 暂时返回第一个订单
        return orders.get(0);
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     * @param reason 取消原因
     * @param operator 操作人
     * @return 是否取消成功
     */
    @Override
    public boolean cancelOrder(String orderNo, String reason, String operator) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return false;
        }

        OrderEvent event;
        if (operator.equals("system")) {
            event = OrderEvent.SYSTEM_CANCEL;
        } else {
            event = OrderEvent.CANCEL;
        }

        return handleOrderEvent(orderNo, event, reason, operator);
    }

    /**
     * 处理超时未支付订单
     */
    @Override
    public void handleTimeoutOrders() {
        List<Order> timeoutOrders = orderRepository.findTimeoutOrders(OrderStatus.PENDING_PAYMENT, 30);
        for (Order order : timeoutOrders) {
            cancelOrder(order.getOrderNo(), "超时未支付", "system");
        }
    }

    /**
     * 生成唯一订单号
     * @return 订单号
     */
    private String generateOrderNo() {
        // 简单实现，实际项目中可以使用更复杂的规则
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 保存订单状态变更日志
     * @param order 订单信息
     * @param toStatus 变更后状态
     * @param reason 变更原因
     * @param operator 操作人
     */
    private void saveOrderStatusLog(Order order, OrderStatus toStatus, String reason, String operator) {
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(order.getId());
        log.setOrderNo(order.getOrderNo());
        log.setFromStatus(order.getStatus());
        log.setToStatus(toStatus);
        log.setReason(reason);
        log.setOperator(operator);
        orderStatusLogRepository.save(log);
    }
}