package com.fc.v2.controller.admin;

import com.fc.v2.model.custom.Inventory;
import com.fc.v2.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存Controller
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 14:40
 */
@Api(tags = "库存管理")
@RestController
@RequestMapping("/admin/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 根据库存ID查询库存信息
     * @param id 库存ID
     * @return 库存信息
     */
    @ApiOperation("根据库存ID查询库存信息")
    @GetMapping("/findById/{id}")
    public Inventory findInventoryById(@PathVariable Long id) {
        return inventoryService.findInventoryById(id);
    }

    /**
     * 根据商品ID和仓库ID查询库存信息
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    @ApiOperation("根据商品ID和仓库ID查询库存信息")
    @GetMapping("/findByProductIdAndWarehouseId")
    public Inventory findInventoryByProductIdAndWarehouseId(@RequestParam Long productId, @RequestParam Long warehouseId) {
        return inventoryService.findInventoryByProductIdAndWarehouseId(productId, warehouseId);
    }

    /**
     * 预占库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 预占数量
     * @return 是否预占成功
     */
    @ApiOperation("预占库存")
    @PostMapping("/reserve")
    public boolean reserveInventory(@RequestParam Long productId, @RequestParam Long warehouseId, @RequestParam Integer quantity) {
        return inventoryService.reserveInventory(productId, warehouseId, quantity);
    }

    /**
     * 释放预占库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 释放数量
     * @return 是否释放成功
     */
    @ApiOperation("释放预占库存")
    @PostMapping("/release")
    public boolean releaseReservedInventory(@RequestParam Long productId, @RequestParam Long warehouseId, @RequestParam Integer quantity) {
        return inventoryService.releaseReservedInventory(productId, warehouseId, quantity);
    }

    /**
     * 扣减库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    @ApiOperation("扣减库存")
    @PostMapping("/deduct")
    public boolean deductInventory(@RequestParam Long productId, @RequestParam Long warehouseId, @RequestParam Integer quantity) {
        return inventoryService.deductInventory(productId, warehouseId, quantity);
    }

    /**
     * 回退库存
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param quantity 回退数量
     * @return 是否回退成功
     */
    @ApiOperation("回退库存")
    @PostMapping("/revert")
    public boolean revertInventory(@RequestParam Long productId, @RequestParam Long warehouseId, @RequestParam Integer quantity) {
        return inventoryService.revertInventory(productId, warehouseId, quantity);
    }
}