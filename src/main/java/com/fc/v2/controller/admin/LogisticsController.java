package com.fc.v2.controller.admin;

import com.fc.v2.model.custom.OrderLogistics;
import com.fc.v2.service.LogisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 物流Controller
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 14:50
 */
@Api(tags = "物流管理")
@RestController
@RequestMapping("/admin/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    /**
     * 根据物流ID查询物流信息
     * @param id 物流ID
     * @return 物流信息
     */
    @ApiOperation("根据物流ID查询物流信息")
    @GetMapping("/findById/{id}")
    public OrderLogistics findLogisticsById(@PathVariable Long id) {
        return logisticsService.findLogisticsById(id);
    }

    /**
     * 根据订单ID查询物流信息
     * @param orderId 订单ID
     * @return 物流信息
     */
    @ApiOperation("根据订单ID查询物流信息")
    @GetMapping("/findByOrderId/{orderId}")
    public OrderLogistics findLogisticsByOrderId(@PathVariable Long orderId) {
        return logisticsService.findLogisticsByOrderId(orderId);
    }

    /**
     * 根据运单号查询物流信息
     * @param trackingNumber 运单号
     * @return 物流信息
     */
    @ApiOperation("根据运单号查询物流信息")
    @GetMapping("/findByTrackingNumber/{trackingNumber}")
    public OrderLogistics findLogisticsByTrackingNumber(@PathVariable String trackingNumber) {
        return logisticsService.findLogisticsByTrackingNumber(trackingNumber);
    }

    /**
     * 选择物流公司
     * @param orderId 订单ID
     * @return 物流公司ID
     */
    @ApiOperation("选择物流公司")
    @PostMapping("/selectCompany/{orderId}")
    public Long selectLogisticsCompany(@PathVariable Long orderId) {
        return logisticsService.selectLogisticsCompany(orderId);
    }

    /**
     * 生成电子面单
     * @param orderId 订单ID
     * @return 电子面单
     */
    @ApiOperation("生成电子面单")
    @PostMapping("/generateWaybill/{orderId}")
    public String generateWaybill(@PathVariable Long orderId) {
        return logisticsService.generateWaybill(orderId);
    }

    /**
     * 更新物流状态
     * @param trackingNumber 运单号
     * @param status 物流状态
     * @return 是否更新成功
     */
    @ApiOperation("更新物流状态")
    @PostMapping("/updateStatus")
    public boolean updateLogisticsStatus(@RequestParam String trackingNumber, @RequestParam String status) {
        return logisticsService.updateLogisticsStatus(trackingNumber, status);
    }

    /**
     * 处理物流状态回调
     * @param trackingNumber 运单号
     * @param status 物流状态
     * @param callbackData 回调数据
     */
    @ApiOperation("处理物流状态回调")
    @PostMapping("/callback")
    public void handleLogisticsCallback(@RequestParam String trackingNumber, @RequestParam String status, @RequestParam String callbackData) {
        logisticsService.handleLogisticsCallback(trackingNumber, status, callbackData);
    }
}