package com.fc.v2.service;

import com.fc.v2.model.custom.OrderLogistics;

/**
 * 物流Service接口
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 12:00
 */
public interface LogisticsService {

    /**
     * 根据物流ID查询物流信息
     * @param id 物流ID
     * @return 物流信息
     */
    OrderLogistics findLogisticsById(Long id);

    /**
     * 根据订单ID查询物流信息
     * @param orderId 订单ID
     * @return 物流信息
     */
    OrderLogistics findLogisticsByOrderId(Long orderId);

    /**
     * 根据运单号查询物流信息
     * @param trackingNumber 运单号
     * @return 物流信息
     */
    OrderLogistics findLogisticsByTrackingNumber(String trackingNumber);

    /**
     * 选择物流公司
     * @param orderId 订单ID
     * @return 物流公司ID
     */
    Long selectLogisticsCompany(Long orderId);

    /**
     * 生成电子面单
     * @param orderId 订单ID
     * @return 电子面单
     */
    String generateWaybill(Long orderId);

    /**
     * 更新物流状态
     * @param trackingNumber 运单号
     * @param status 物流状态
     * @return 是否更新成功
     */
    boolean updateLogisticsStatus(String trackingNumber, String status);

    /**
     * 处理物流状态回调
     * @param trackingNumber 运单号
     * @param status 物流状态
     * @param callbackData 回调数据
     */
    void handleLogisticsCallback(String trackingNumber, String status, String callbackData);
}