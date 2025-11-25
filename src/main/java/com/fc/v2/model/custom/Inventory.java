package com.fc.v2.model.custom;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 库存实体类
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 10:40
 */
@Entity
@Table(name = "t_inventory")
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库存ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品ID
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 可用库存
     */
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    /**
     * 预占库存
     */
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

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

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
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

    // 构造方法
    public Inventory() {
        this.createTime = new Date();
        this.updateTime = new Date();
        this.availableQuantity = 0;
        this.reservedQuantity = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }
}