package com.fc.v2.mapper.custom;

import com.fc.v2.model.custom.OrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单状态变更日志Repository接口
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 11:30
 */
@Repository
public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Long> {

    /**
     * 根据订单ID查询订单状态变更日志列表
     * @param orderId 订单ID
     * @return 订单状态变更日志列表
     */
    List<OrderStatusLog> findByOrderId(Long orderId);

    /**
     * 根据订单号查询订单状态变更日志列表
     * @param orderNo 订单号
     * @return 订单状态变更日志列表
     */
    List<OrderStatusLog> findByOrderNo(String orderNo);
}