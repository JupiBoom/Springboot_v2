package com.fc.v2.model.custom;

/**
 * 订单事件枚举
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 10:05
 */
public enum OrderEvent {
    /**
     * 用户支付订单
     */
    PAY,
    /**
     * 商家发货
     */
    SHIP,
    /**
     * 用户确认收货
     */
    CONFIRM_RECEIPT,
    /**
     * 用户取消订单
     */
    CANCEL,
    /**
     * 系统取消订单（超时未支付）
     */
    SYSTEM_CANCEL
}