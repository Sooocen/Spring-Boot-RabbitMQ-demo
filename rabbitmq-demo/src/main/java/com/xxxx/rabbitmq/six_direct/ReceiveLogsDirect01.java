package com.xxxx.rabbitmq.six_direct;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

/**
 * @Author: Socen
 * @Description:
 * 直接交换模式direct
 * 消费者 接收Log1
 */
public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
        //绑定路由Key
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        /**
         * 声明回调函数 deliverCallback接受消息  cancelCallback取消接受
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("01接收到的消息为:"+new String(message.getBody()));
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
        channel.basicConsume("disk",deliverCallback,cancelCallback);


    }
}
