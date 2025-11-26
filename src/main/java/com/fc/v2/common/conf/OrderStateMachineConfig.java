package com.fc.v2.common.conf;

import com.fc.v2.common.enumclass.OrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatusEnum, String> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatusEnum, String> config) throws Exception {
        config.withConfiguration()
                .listener(new StateMachineListenerAdapter<OrderStatusEnum, String>() {
                    @Override
                    public void stateChanged(State<OrderStatusEnum, String> from, State<OrderStatusEnum, String> to) {
                        System.out.println("状态变更: " + (from != null ? from.getId() : "null") + " -> " + to.getId());
                    }
                });
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatusEnum, String> states) throws Exception {
        states.withStates()
                .initial(OrderStatusEnum.PENDING_PAYMENT)
                .states(EnumSet.allOf(OrderStatusEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatusEnum, String> transitions) throws Exception {
        transitions
                // 待支付 -> 已支付
                .withExternal()
                .source(OrderStatusEnum.PENDING_PAYMENT)
                .target(OrderStatusEnum.PAID)
                .event("PAY")
                .and()
                // 已支付 -> 已发货
                .withExternal()
                .source(OrderStatusEnum.PAID)
                .target(OrderStatusEnum.SHIPPED)
                .event("SHIP")
                .and()
                // 已发货 -> 已完成
                .withExternal()
                .source(OrderStatusEnum.SHIPPED)
                .target(OrderStatusEnum.COMPLETED)
                .event("CONFIRM_RECEIPT")
                .and()
                // 待支付 -> 已取消
                .withExternal()
                .source(OrderStatusEnum.PENDING_PAYMENT)
                .target(OrderStatusEnum.CANCELLED)
                .event("CANCEL")
                .and()
                // 已支付 -> 已取消（发货前可取消）
                .withExternal()
                .source(OrderStatusEnum.PAID)
                .target(OrderStatusEnum.CANCELLED)
                .event("CANCEL");
    }
}