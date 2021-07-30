package com.xxxx.rabbitmq.spirngbootrabbitmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ
 * 利用插件使用延迟发送配置类
 * controller  SendMessageController
 * consumer  DelayQueueConsumer
 */
@Configuration
public class DelayedQueueConfig {
    //延迟队列名称
    public static final String DELAYED_QUEUE_NAME= "delayed.queue";
    //延迟交换机名称
    public static final String DELAYED_EXCHANGE_NAME= "delayed.exchange";
    //延迟交换机RoutingKey
    public static final String DELAYED_ROUTING_KEY= "delayed.routingkey";

    //声明队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //声明交换机
    @Bean
    public CustomExchange delayedExchange(){
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他参数
         */  //定义参数
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue, @Qualifier("delayedExchange")CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
