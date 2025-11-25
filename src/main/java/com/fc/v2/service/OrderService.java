package com.fc.v2.service;

import com.fc.v2.model.custom.Order;
import com.fc.v2.model.custom.OrderEvent;
import com.fc.v2.model.custom.OrderStatus;

import java.util.List;

/**
 * 订单Service接口
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 11:40
 */
public interface OrderService {

    /**
     * 创建订单
     * @param order 订单信息
     * @return 订单信息
     */
    Order createOrder(Order order);

    /**
     * 根据订单ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    Order findOrderById(Long id);

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order findOrderByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> findOrdersByUserId(Long userId);

    /**
     * 根据订单状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findOrdersByStatus(OrderStatus status);

    /**
     * 处理订单状态变更
     * @param orderNo 订单号
     * @param event 订单事件
     * @param reason 变更原因
     * @param operator 操作人
     * @return 是否处理成功
     */
    boolean handleOrderEvent(String orderNo, OrderEvent event, String reason, String operator);

    /**
     * 拆分订单
     * @param order 原始订单
     * @return 拆分后的订单列表
     */
    List<Order> splitOrder(Order order);

    /**
     * 合并订单
     * @param orders 订单列表
     * @return 合并后的订单
     */
    Order mergeOrders(List<Order> orders);

    /**
     * 取消订单
     * @param orderNo 订单号
     * @param reason 取消原因
     * @param operator 操作人
     * @return 是否取消成功
     */
    boolean cancelOrder(String orderNo, String reason, String operator);

    /**
     * 处理超时未支付订单
     */
    void handleTimeoutOrders();
}