package com.xxxx.rabbitmq.eight_dead;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Socen
 * @Description:
 * 死信模式dead
 * 死信队列消费者 启动后 接收死信消息
 */
public class Consumer02 {

    //声明死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //声明死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    //接收消息
    public static void main(String[] args) throws Exception {
        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("等待接收消息......");

        /**
         * 声明回调函数 deliverCallback接受消息  cancelCallback取消接受
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("02接收到的消息为:"+new String(message.getBody()));
            System.out.println("接收队列为"+ DEAD_EXCHANGE+"   接收消息的路由Key为:"+message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = consumerTag->{
            System.out.println(consumerTag + "消息消费被中断了喔");
        };
        /**
         * @Description: 接受消息
         * @Prame:
         * 1.消费队列名称 QUEUE_NAME(hello)
         * 2.消费成功后是否自动应答(true)  手动应答(false)
         * 3.消费者成功消费回调
         * 4.消费者取消消费的回调
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
    }
}
