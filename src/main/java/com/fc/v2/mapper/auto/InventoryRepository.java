package com.fc.v2.mapper.auto;

import com.fc.v2.model.auto.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {
    Inventory findByProductSkuAndWarehouseId(String productSku, Long warehouseId);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.availableStock = i.availableStock - :quantity, i.reservedStock = i.reservedStock + :quantity WHERE i.productSku = :productSku AND i.warehouseId = :warehouseId AND i.availableStock >= :quantity")
    int reserveInventory(@Param("productSku") String productSku, @Param("warehouseId") Long warehouseId, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.availableStock = i.availableStock + :quantity, i.reservedStock = i.reservedStock - :quantity WHERE i.productSku = :productSku AND i.warehouseId = :warehouseId AND i.reservedStock >= :quantity")
    int releaseReservedInventory(@Param("productSku") String productSku, @Param("warehouseId") Long warehouseId, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.reservedStock = i.reservedStock - :quantity WHERE i.productSku = :productSku AND i.warehouseId = :warehouseId AND i.reservedStock >= :quantity")
    int deductInventory(@Param("productSku") String productSku, @Param("warehouseId") Long warehouseId, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.availableStock = i.availableStock + :quantity WHERE i.productSku = :productSku AND i.warehouseId = :warehouseId")
    int rollbackInventory(@Param("productSku") String productSku, @Param("warehouseId") Long warehouseId, @Param("quantity") Integer quantity);
}