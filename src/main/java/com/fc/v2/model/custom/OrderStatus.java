package com.fc.v2.model.custom;

/**
 * 订单状态枚举
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 10:00
 */
public enum OrderStatus {
    /**
     * 待支付
     */
    PENDING_PAYMENT,
    /**
     * 已支付
     */
    PAID,
    /**
     * 已发货
     */
    SHIPPED,
    /**
     * 已完成
     */
    COMPLETED,
    /**
     * 已取消
     */
    CANCELLED
}