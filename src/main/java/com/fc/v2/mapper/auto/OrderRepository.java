package com.fc.v2.mapper.auto;

import com.fc.v2.model.auto.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Order findByOrderNo(String orderNo);
    List<Order> findByStatus(String status);
}