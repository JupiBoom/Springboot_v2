package com.fc.v2.service;

import com.fc.v2.model.auto.LogisticsInfo;

public interface LogisticsService {
    /**
     * 选择物流公司
     * @param orderId 订单ID
     * @return 物流公司代码
     */
    String selectLogisticsCompany(Long orderId);

    /**
     * 生成电子面单
     * @param orderId 订单ID
     * @return 电子面单信息
     */
    LogisticsInfo generateWaybill(Long orderId);

    /**
     * 跟踪物流状态
     * @param trackingNumber 运单号
     * @return 物流状态信息
     */
    LogisticsInfo trackLogisticsStatus(String trackingNumber);

    /**
     * 处理物流状态回调
     * @param logisticsInfo 物流状态信息
     */
    void handleLogisticsCallback(LogisticsInfo logisticsInfo);
}