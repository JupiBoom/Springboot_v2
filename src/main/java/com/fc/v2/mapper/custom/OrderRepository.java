package com.fc.v2.mapper.custom;

import com.fc.v2.model.custom.Order;
import com.fc.v2.model.custom.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单Repository接口
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 11:10
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order findByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> findByUserId(Long userId);

    /**
     * 根据订单状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * 查询超时未支付的订单
     * @param status 订单状态
     * @param minutes 超时分钟数
     * @return 超时未支付的订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createTime < DATE_SUB(NOW(), INTERVAL :minutes MINUTE)")
    List<Order> findTimeoutOrders(@Param("status") OrderStatus status, @Param("minutes") Integer minutes);
}