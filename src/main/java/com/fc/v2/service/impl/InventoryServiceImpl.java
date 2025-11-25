package com.fc.v2.service.impl;

import com.fc.v2.mapper.custom.InventoryRepository;
import com.fc.v2.model.custom.Inventory;
import com.fc.v2.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存Service实现类
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 14:10
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 根据库存ID查询库存信息
     * @param id 库存ID
     * @return 库存信息
     */
    @Override
    public Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    /**
     * 根据商品ID和仓库ID查询库存信息
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    @Override
    public Inventory findInventoryByProductIdAndWarehouseId(Long productId, Long warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseIdForUpdate(productId, warehouseId);
    }

    /**
     * 预占库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 预占数量
     * @return 是否预占成功
     */
    @Override
    public boolean reserveInventory(Long productId, Long warehouseId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseIdForUpdate(productId, warehouseId);
        if (inventory == null || inventory.getAvailableQuantity() < quantity) {
            return false;
        }

        // 预占库存
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        return true;
    }

    /**
     * 释放预占库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 释放数量
     * @return 是否释放成功
     */
    @Override
    public boolean releaseReservedInventory(Long productId, Long warehouseId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseIdForUpdate(productId, warehouseId);
        if (inventory == null || inventory.getReservedQuantity() < quantity) {
            return false;
        }

        // 释放预占库存
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventoryRepository.save(inventory);
        return true;
    }

    /**
     * 扣减库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    @Override
    public boolean deductInventory(Long productId, Long warehouseId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseIdForUpdate(productId, warehouseId);
        if (inventory == null || inventory.getReservedQuantity() < quantity) {
            return false;
        }

        // 扣减库存
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventoryRepository.save(inventory);
        return true;
    }

    /**
     * 回退库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 回退数量
     * @return 是否回退成功
     */
    @Override
    public boolean revertInventory(Long productId, Long warehouseId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseIdForUpdate(productId, warehouseId);
        if (inventory == null) {
            return false;
        }

        // 回退库存
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventoryRepository.save(inventory);
        return true;
    }
}