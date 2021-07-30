package com.xxxx.rabbitmq.seven_topic;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

/**
 * @Author: Socen
 * @Description:
 * 主题模式topic
 * 消费者 接收Log1
 */
public class Consumer01 {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //接收消息
    public static void main(String[] args) throws Exception {
        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //声明一个队列(队列一)
        String queueName = "Q1";
        /**
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        channel.queueDeclare(queueName,false,false,false,null);
        //(主题模式)绑定路由Key
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("01等待接受消息喔");

        /**
         * 声明回调函数 deliverCallback接受消息  cancelCallback取消接受
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("01接收到的消息为:"+new String(message.getBody()));
            System.out.println("接收队列为"+queueName+"   接收消息的路由Key为:"+message.getEnvelope().getRoutingKey());
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
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);

    }

}
