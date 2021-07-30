package com.xxxx.rabbitmq.spirngbootrabbitmqdemo.controller;

import com.xxxx.rabbitmq.spirngbootrabbitmqdemo.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 * 延迟10S
 *
 *  http://localhost:8080/ttl/sendMsg/嘻嘻嘻
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;
    //发消息
    @RequestMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间 :{},发送一条消息给两个TTL队列{}",new Date().toString(),message);

        rabbitTemplate.convertAndSend("X","XA","10S的TTL消息:"+message);
        rabbitTemplate.convertAndSend("X","XB","40S的TTL消息:"+message);
    }
    //发送消息(含过期时间)
    @RequestMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间 :{},发送一条时长为{}mx的TTL消息给QC队列{}",new Date().toString(),ttlTime,message);

        rabbitTemplate.convertAndSend("X","XC",message,msg->{
            //设置发送消息的延迟时长 (有缺陷)
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    //!!!!!!!!!!!!!!  基于插件的消息发送  !!!!!!!!
    @RequestMapping("/sendDelayedMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message,@PathVariable Integer delayTime){
        log.info("当前时间 :{},发送一条延迟{}的延迟消息给延迟队列 内容为：{}",new Date().toString(),delayTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY,message,msg->{
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    //!!!!!!!!!!!!!!  发布确认消息发送  !!!!!!!!!!!!!!

    }
}
