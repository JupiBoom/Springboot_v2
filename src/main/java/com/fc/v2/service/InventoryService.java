package com.fc.v2.service;

import com.fc.v2.model.auto.Inventory;

public interface InventoryService {
    /**
     * 根据商品SKU和仓库ID查询库存
     * @param productSku 商品SKU
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    Inventory findByProductSkuAndWarehouseId(String productSku, Long warehouseId);

    /**
     * 预占库存
     * @param productSku 商品SKU
     * @param warehouseId 仓库ID
     * @param quantity 预占数量
     * @return 是否预占成功
     */
    boolean reserveInventory(String productSku, Long warehouseId, Integer quantity);

    /**
     * 释放预占库存
     * @param productSku 商品SKU
     * @param warehouseId 仓库ID
     * @param quantity 释放数量
     * @return 是否释放成功
     */
    boolean releaseReservedInventory(String productSku, Long warehouseId, Integer quantity);

    /**
     * 实际扣减库存
     * @param productSku 商品SKU
     * @param warehouseId 仓库ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    boolean deductInventory(String productSku, Long warehouseId, Integer quantity);

    /**
     * 回退库存
     * @param productSku 商品SKU
     * @param warehouseId 仓库ID
     * @param quantity 回退数量
     * @return 是否回退成功
     */
    boolean rollbackInventory(String productSku, Long warehouseId, Integer quantity);
}