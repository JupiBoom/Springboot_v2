package com.fc.v2.mapper.auto;

import com.fc.v2.model.auto.LogisticsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsRepository extends JpaRepository<LogisticsInfo, Long>, JpaSpecificationExecutor<LogisticsInfo> {
    LogisticsInfo findByOrderId(Long orderId);
    LogisticsInfo findByTrackingNumber(String trackingNumber);
}