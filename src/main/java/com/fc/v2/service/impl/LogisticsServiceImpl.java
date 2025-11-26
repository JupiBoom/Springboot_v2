package com.fc.v2.service.impl;

import com.fc.v2.mapper.auto.LogisticsRepository;
import com.fc.v2.model.auto.LogisticsInfo;
import com.fc.v2.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
public class LogisticsServiceImpl implements LogisticsService {

    @Autowired
    private LogisticsRepository logisticsRepository;

    @Override
    public String selectLogisticsCompany(Long orderId) {
        // TODO: 实现物流公司选择策略（成本/时效最优）
        // 暂时返回默认物流公司
        return "SF_EXPRESS"; // 顺丰速运
    }

    @Override
    @Transactional
    public LogisticsInfo generateWaybill(Long orderId) {
        // 选择物流公司
        String logisticsCompany = selectLogisticsCompany(orderId);

        // 生成运单号
        String trackingNumber = generateTrackingNumber(logisticsCompany);

        // 生成电子面单
        LogisticsInfo logisticsInfo = new LogisticsInfo();
        logisticsInfo.setOrderId(orderId);
        logisticsInfo.setLogisticsCompany(logisticsCompany);
        logisticsInfo.setTrackingNumber(trackingNumber);
        logisticsInfo.setWaybillNumber(generateWaybillNumber());
        logisticsInfo.setStatus("CREATED");
        logisticsInfo.setCreateTime(new Date());
        logisticsInfo.setUpdateTime(new Date());

        return logisticsRepository.save(logisticsInfo);
    }

    @Override
    public LogisticsInfo trackLogisticsStatus(String trackingNumber) {
        // TODO: 调用物流公司API查询物流状态
        // 暂时返回模拟数据
        LogisticsInfo logisticsInfo = logisticsRepository.findByTrackingNumber(trackingNumber);
        if (logisticsInfo != null) {
            // 模拟物流状态更新
            String[] statuses = {"CREATED", "IN_TRANSIT", "OUT_FOR_DELIVERY", "DELIVERED"};
            int randomIndex = new Random().nextInt(statuses.length);
            logisticsInfo.setStatus(statuses[randomIndex]);
            logisticsInfo.setUpdateTime(new Date());
            logisticsRepository.save(logisticsInfo);
        }
        return logisticsInfo;
    }

    @Override
    @Transactional
    public void handleLogisticsCallback(LogisticsInfo logisticsInfo) {
        // 处理物流状态回调
        LogisticsInfo existingInfo = logisticsRepository.findByTrackingNumber(logisticsInfo.getTrackingNumber());
        if (existingInfo != null) {
            existingInfo.setStatus(logisticsInfo.getStatus());
            existingInfo.setUpdateTime(new Date());
            logisticsRepository.save(existingInfo);

            // TODO: 根据物流状态更新订单状态
            // 例如：如果物流状态为DELIVERED，更新订单状态为COMPLETED
        }
    }

    /**
     * 生成运单号
     * @param logisticsCompany 物流公司代码
     * @return 运单号
     */
    private String generateTrackingNumber(String logisticsCompany) {
        // 模拟生成运单号
        // 顺丰速运运单号格式：12位数字
        if ("SF_EXPRESS".equals(logisticsCompany)) {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 12; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        }
        // 其他物流公司运单号格式
        return "TN" + System.currentTimeMillis();
    }

    /**
     * 生成电子面单号
     * @return 电子面单号
     */
    private String generateWaybillNumber() {
        // 模拟生成电子面单号
        return "WB" + System.currentTimeMillis() + new Random().nextInt(1000);
    }
}