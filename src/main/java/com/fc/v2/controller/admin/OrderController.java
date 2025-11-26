package com.fc.v2.controller.admin;

import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.model.auto.Order;
import com.fc.v2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param order 订单信息
     * @return 响应结果
     */
    @PostMapping("/create")
    public AjaxResult createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            AjaxResult result = AjaxResult.success("订单创建成功");
            result.put("data", createdOrder);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("订单创建失败: " + e.getMessage());
        }
    }

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 响应结果
     */
    @GetMapping("/findByOrderNo/{orderNo}")
    public AjaxResult findByOrderNo(@PathVariable String orderNo) {
        try {
            Order order = orderService.findByOrderNo(orderNo);
            if (order != null) {
                AjaxResult result = AjaxResult.success("订单查询成功");
                result.put("data", order);
                return result;
            } else {
                return AjaxResult.error("订单不存在");
            }
        } catch (Exception e) {
            return AjaxResult.error("订单查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 响应结果
     */
    @GetMapping("/findByUserId/{userId}")
    public AjaxResult findByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            AjaxResult result = AjaxResult.success("订单列表查询成功");
            result.put("data", orders);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("订单列表查询失败: " + e.getMessage());
        }
    }

    /**
     * 支付订单
     * @param orderNo 订单号
     * @return 响应结果
     */
    @PostMapping("/pay/{orderNo}")
    public AjaxResult payOrder(@PathVariable String orderNo) {
        try {
            Order paidOrder = orderService.payOrder(orderNo);
            AjaxResult result = AjaxResult.success("订单支付成功");
            result.put("data", paidOrder);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("订单支付失败: " + e.getMessage());
        }
    }

    /**
     * 发货订单
     * @param orderNo 订单号
     * @return 响应结果
     */
    @PostMapping("/ship/{orderNo}")
    public AjaxResult shipOrder(@PathVariable String orderNo) {
        try {
            Order shippedOrder = orderService.shipOrder(orderNo);
            AjaxResult result = AjaxResult.success("订单发货成功");
            result.put("data", shippedOrder);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("订单发货失败: " + e.getMessage());
        }
    }

    /**
     * 确认收货
     * @param orderNo 订单号
     * @return 响应结果
     */
    @PostMapping("/confirmReceipt/{orderNo}")
    public AjaxResult confirmReceipt(@PathVariable String orderNo) {
        try {
            Order completedOrder = orderService.confirmReceipt(orderNo);
            AjaxResult result = AjaxResult.success("确认收货成功");
            result.put("data", completedOrder);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("确认收货失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     * @return 响应结果
     */
    @PostMapping("/cancel/{orderNo}")
    public AjaxResult cancelOrder(@PathVariable String orderNo) {
        try {
            Order cancelledOrder = orderService.cancelOrder(orderNo);
            AjaxResult result = AjaxResult.success("订单取消成功");
            result.put("data", cancelledOrder);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("订单取消失败: " + e.getMessage());
        }
    }

    /**
     * 统计订单处理时效
     * @return 响应结果
     */
    @GetMapping("/statistics/processingTime")
    public AjaxResult statisticsOrderProcessingTime() {
        try {
            Map<String, Object> statistics = orderService.statisticsOrderProcessingTime();
            AjaxResult result = AjaxResult.success("订单处理时效统计成功");
            result.put("data", statistics);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("订单处理时效统计失败: " + e.getMessage());
        }
    }

    /**
     * 统计物流时效
     * @return 响应结果
     */
    @GetMapping("/statistics/logisticsTime")
    public AjaxResult statisticsLogisticsTime() {
        try {
            Map<String, Object> statistics = orderService.statisticsLogisticsTime();
            AjaxResult result = AjaxResult.success("物流时效统计成功");
            result.put("data", statistics);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("物流时效统计失败: " + e.getMessage());
        }
    }

    /**
     * 分析异常订单
     * @return 响应结果
     */
    @GetMapping("/analysis/exceptionOrders")
    public AjaxResult analyzeExceptionOrders() {
        try {
            Map<String, Object> analysis = orderService.analyzeExceptionOrders();
            AjaxResult result = AjaxResult.success("异常订单分析成功");
            result.put("data", analysis);
            return result;
        } catch (Exception e) {
            return AjaxResult.error("异常订单分析失败: " + e.getMessage());
        }
    }
}