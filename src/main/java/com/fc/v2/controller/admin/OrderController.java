package com.fc.v2.controller.admin;

import com.fc.v2.model.custom.Order;
import com.fc.v2.model.custom.OrderEvent;
import com.fc.v2.model.custom.OrderStatus;
import com.fc.v2.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单Controller
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 14:30
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param order 订单信息
     * @return 订单信息
     */
    @ApiOperation("创建订单")
    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    /**
     * 根据订单ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    @ApiOperation("根据订单ID查询订单")
    @GetMapping("/findById/{id}")
    public Order findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    @ApiOperation("根据订单号查询订单")
    @GetMapping("/findByOrderNo/{orderNo}")
    public Order findOrderByOrderNo(@PathVariable String orderNo) {
        return orderService.findOrderByOrderNo(orderNo);
    }

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @ApiOperation("根据用户ID查询订单列表")
    @GetMapping("/findByUserId/{userId}")
    public List<Order> findOrdersByUserId(@PathVariable Long userId) {
        return orderService.findOrdersByUserId(userId);
    }

    /**
     * 根据订单状态查询订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    @ApiOperation("根据订单状态查询订单列表")
    @GetMapping("/findByStatus/{status}")
    public List<Order> findOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.findOrdersByStatus(status);
    }

    /**
     * 处理订单支付
     * @param orderNo 订单号
     * @return 是否处理成功
     */
    @ApiOperation("处理订单支付")
    @PostMapping("/pay/{orderNo}")
    public boolean handleOrderPay(@PathVariable String orderNo) {
        return orderService.handleOrderEvent(orderNo, OrderEvent.PAY, "用户支付订单", "user");
    }

    /**
     * 处理订单发货
     * @param orderNo 订单号
     * @return 是否处理成功
     */
    @ApiOperation("处理订单发货")
    @PostMapping("/ship/{orderNo}")
    public boolean handleOrderShip(@PathVariable String orderNo) {
        return orderService.handleOrderEvent(orderNo, OrderEvent.SHIP, "商家发货", "admin");
    }

    /**
     * 处理订单确认收货
     * @param orderNo 订单号
     * @return 是否处理成功
     */
    @ApiOperation("处理订单确认收货")
    @PostMapping("/confirm/{orderNo}")
    public boolean handleOrderConfirm(@PathVariable String orderNo) {
        return orderService.handleOrderEvent(orderNo, OrderEvent.CONFIRM_RECEIPT, "用户确认收货", "user");
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     * @param reason 取消原因
     * @return 是否取消成功
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancel/{orderNo}")
    public boolean cancelOrder(@PathVariable String orderNo, @RequestParam String reason) {
        return orderService.cancelOrder(orderNo, reason, "user");
    }

    /**
     * 拆分订单
     * @param order 原始订单
     * @return 拆分后的订单列表
     */
    @ApiOperation("拆分订单")
    @PostMapping("/split")
    public List<Order> splitOrder(@RequestBody Order order) {
        return orderService.splitOrder(order);
    }

    /**
     * 合并订单
     * @param orders 订单列表
     * @return 合并后的订单
     */
    @ApiOperation("合并订单")
    @PostMapping("/merge")
    public Order mergeOrders(@RequestBody List<Order> orders) {
        return orderService.mergeOrders(orders);
    }
}