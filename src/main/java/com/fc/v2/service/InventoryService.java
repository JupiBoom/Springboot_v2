package com.fc.v2.service;

import com.fc.v2.model.custom.Inventory;

/**
 * 库存Service接口
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 11:50
 */
public interface InventoryService {

    /**
     * 根据库存ID查询库存信息
     * @param id 库存ID
     * @return 库存信息
     */
    Inventory findInventoryById(Long id);

    /**
     * 根据商品ID和仓库ID查询库存信息
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    Inventory findInventoryByProductIdAndWarehouseId(Long productId, Long warehouseId);

    /**
     * 预占库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 预占数量
     * @return 是否预占成功
     */
    boolean reserveInventory(Long productId, Long warehouseId, Integer quantity);

    /**
     * 释放预占库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 释放数量
     * @return 是否释放成功
     */
    boolean releaseReservedInventory(Long productId, Long warehouseId, Integer quantity);

    /**
     * 扣减库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    boolean deductInventory(Long productId, Long warehouseId, Integer quantity);

    /**
     * 回退库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 回退数量
     * @return 是否回退成功
     */
    boolean revertInventory(Long productId, Long warehouseId, Integer quantity);
}