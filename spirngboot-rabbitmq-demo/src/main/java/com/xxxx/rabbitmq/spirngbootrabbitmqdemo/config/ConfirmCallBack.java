package com.xxxx.rabbitmq.spirngbootrabbitmqdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消息确认回调接收
 */

@Slf4j
@Component
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //注入RabbitTemplate
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1.给交换机发送消息 交换机收到启用回调
     *  1.1 correlationData 保存回调信息的ID及相关信息
     *  1.2 交换机是否收到消息 收到消息(true) 没有收到消息(false)
     *  1.3 cause(造成原因) 如果成功则为空值(null)
     * 2.给交换机发送消息 交换机未收到消息启用回调
     *  2.1 correlationData 保存回调信息的ID及相关信息
     *  2.2 交换机是否收到消息  收到消息(true) 没有收到消息(false)
     *  2.3 收到失败所造成的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId():"";
        if(ack){
            log.info("交换机成功收到了id为{}的消息",id);
        }else{
            log.info("交换机没有收到id为{}的消息 由于{}",id,cause);
        }
    }

    /**
     * 消息无法传递到目的地时将消息返回给生产者(仅不可达时传递此消息)
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息{}被交换机{}回退,回退原因为:{},RoutingKey为:{}",returnedMessage.getMessage().toString(),returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }
}
