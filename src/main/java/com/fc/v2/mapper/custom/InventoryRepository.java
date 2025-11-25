package com.fc.v2.mapper.custom;

import com.fc.v2.model.custom.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * 库存Repository接口
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 11:20
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * 根据商品ID和仓库ID查询库存信息（悲观锁）
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productId = :productId AND i.warehouseId = :warehouseId")
    Inventory findByProductIdAndWarehouseIdForUpdate(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    /**
     * 根据商品ID查询库存信息
     * @param productId 商品ID
     * @return 库存信息
     */
    Inventory findByProductId(Long productId);
}