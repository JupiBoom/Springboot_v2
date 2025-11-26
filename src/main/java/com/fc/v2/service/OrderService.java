package com.fc.v2.service;

import com.fc.v2.model.auto.Order;
import java.util.List;
import java.util.Map;

public interface OrderService {
    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建后的订单
     */
    Order createOrder(Order order);

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order findByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> findByUserId(Long userId);

    /**
     * 支付订单
     * @param orderNo 订单号
     * @return 支付后的订单
     */
    Order payOrder(String orderNo);

    /**
     * 发货订单
     * @param orderNo 订单号
     * @return 发货后的订单
     */
    Order shipOrder(String orderNo);

    /**
     * 确认收货
     * @param orderNo 订单号
     * @return 确认后的订单
     */
    Order confirmReceipt(String orderNo);

    /**
     * 取消订单
     * @param orderNo 订单号
     * @return 取消后的订单
     */
    Order cancelOrder(String orderNo);

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
     * 统计订单处理时效
     * @return 时效统计结果
     */
    Map<String, Object> statisticsOrderProcessingTime();

    /**
     * 统计物流时效
     * @return 物流时效统计结果
     */
    Map<String, Object> statisticsLogisticsTime();

    /**
     * 分析异常订单
     * @return 异常订单分析结果
     */
    Map<String, Object> analyzeExceptionOrders();
}