package com.fc.v2.service.impl;

import com.fc.v2.mapper.auto.InventoryRepository;
import com.fc.v2.model.auto.Inventory;
import com.fc.v2.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Inventory findByProductSkuAndWarehouseId(String productSku, Long warehouseId) {
        return inventoryRepository.findByProductSkuAndWarehouseId(productSku, warehouseId);
    }

    @Override
    public boolean reserveInventory(String productSku, Long warehouseId, Integer quantity) {
        int result = inventoryRepository.reserveInventory(productSku, warehouseId, quantity);
        return result > 0;
    }

    @Override
    public boolean releaseReservedInventory(String productSku, Long warehouseId, Integer quantity) {
        int result = inventoryRepository.releaseReservedInventory(productSku, warehouseId, quantity);
        return result > 0;
    }

    @Override
    public boolean deductInventory(String productSku, Long warehouseId, Integer quantity) {
        int result = inventoryRepository.deductInventory(productSku, warehouseId, quantity);
        return result > 0;
    }

    @Override
    public boolean rollbackInventory(String productSku, Long warehouseId, Integer quantity) {
        int result = inventoryRepository.rollbackInventory(productSku, warehouseId, quantity);
        return result > 0;
    }
}