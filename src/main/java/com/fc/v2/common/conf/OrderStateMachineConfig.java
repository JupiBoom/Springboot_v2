package com.fc.v2.common.conf;

import com.fc.v2.model.custom.OrderEvent;
import com.fc.v2.model.custom.OrderStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * 订单状态机配置
 * @author fuce
 * @version 1.0
 * @date 2023/10/25 10:10
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    /**
     * 配置状态
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.PENDING_PAYMENT)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    /**
     * 配置状态转换事件关系
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                // 待支付 -> 已支付
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT)
                .target(OrderStatus.PAID)
                .event(OrderEvent.PAY)
                .and()
                // 已支付 -> 已发货
                .withExternal()
                .source(OrderStatus.PAID)
                .target(OrderStatus.SHIPPED)
                .event(OrderEvent.SHIP)
                .and()
                // 已发货 -> 已完成
                .withExternal()
                .source(OrderStatus.SHIPPED)
                .target(OrderStatus.COMPLETED)
                .event(OrderEvent.CONFIRM_RECEIPT)
                .and()
                // 待支付 -> 已取消（用户取消）
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT)
                .target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL)
                .and()
                // 待支付 -> 已取消（系统取消）
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT)
                .target(OrderStatus.CANCELLED)
                .event(OrderEvent.SYSTEM_CANCEL);
    }

    /**
     * 配置状态机监听器
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
        StateMachineListenerAdapter<OrderStatus, OrderEvent> listenerAdapter = new StateMachineListenerAdapter<OrderStatus, OrderEvent>() {
            @Override
            public void stateChanged(State<OrderStatus, OrderEvent> from, State<OrderStatus, OrderEvent> to) {
                System.out.println("订单状态变更: " + (from != null ? from.getId() : "初始状态") + " -> " + to.getId());
            }
        };
        config.withConfiguration().listener(listenerAdapter);
    }
}