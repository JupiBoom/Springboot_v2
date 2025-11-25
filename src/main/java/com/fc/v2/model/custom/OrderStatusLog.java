package com.fc.v2.model.custom;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态变更日志实体类
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 11:00
 */
@Entity
@Table(name = "t_order_status_log")
public class OrderStatusLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 订单号
     */
    @Column(name = "order_no", nullable = false)
    private String orderNo;

    /**
     * 变更前状态
     */
    @Column(name = "from_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus fromStatus;

    /**
     * 变更后状态
     */
    @Column(name = "to_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus toStatus;

    /**
     * 变更原因
     */
    @Column(name = "reason", nullable = false)
    private String reason;

    /**
     * 操作人
     */
    @Column(name = "operator", nullable = false)
    private String operator;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private Date createTime;

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(OrderStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public OrderStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(OrderStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // 构造方法
    public OrderStatusLog() {
        this.createTime = new Date();
    }
}