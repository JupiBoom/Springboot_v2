package com.fc.v2.model.custom;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单物流实体类
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 10:50
 */
@Entity
@Table(name = "t_order_logistics")
public class OrderLogistics implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物流ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;

    /**
     * 物流公司ID
     */
    @Column(name = "logistics_company_id", nullable = false)
    private Long logisticsCompanyId;

    /**
     * 物流公司名称
     */
    @Column(name = "logistics_company_name", nullable = false)
    private String logisticsCompanyName;

    /**
     * 运单号
     */
    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    /**
     * 电子面单
     */
    @Column(name = "waybill", columnDefinition = "TEXT")
    private String waybill;

    /**
     * 物流状态
     */
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    /**
     * 订单关联
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

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

    public Long getLogisticsCompanyId() {
        return logisticsCompanyId;
    }

    public void setLogisticsCompanyId(Long logisticsCompanyId) {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    public String getLogisticsCompanyName() {
        return logisticsCompanyName;
    }

    public void setLogisticsCompanyName(String logisticsCompanyName) {
        this.logisticsCompanyName = logisticsCompanyName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // 构造方法
    public OrderLogistics() {
        this.createTime = new Date();
        this.updateTime = new Date();
        this.status = "PENDING";
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }
}