package com.xxxx.rabbitmq.spirngbootrabbitmqdemo.controller;

import com.xxxx.rabbitmq.spirngbootrabbitmqdemo.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //发送确认消息
    @RequestMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
    CorrelationData correlationData = new CorrelationData("1");
    rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData);
    log.info("发送的消息为:{}",message);
    //发送一个无法到达路由器的消息 路由到备份交换机 备份机GG 走回退
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY+"2",message,correlationData2);
        log.info("发送的消息为:{}",message);

    }
}
