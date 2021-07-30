package com.xxxx.rabbitmq.nine;

import com.rabbitmq.client.*;

public class Consumer {

    //队列名称
    public static final String QUEUE_NAME="hello";

    //接受消息
    public static void main(String[] args) throws Exception {

        //创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置工厂IP 连接RabbitMQ队列  192.168.8.53为本机IP
        connectionFactory.setHost("localhost");
        //设置用户名 密码
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        //创建连接
        Connection connection = connectionFactory.newConnection();
        //获取通信信道
        Channel channel = connection.createChannel();



        /**
         * 声明回调函数 deliverCallback  cancelCallback
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断了喔");
        };

        /**
         * @Description: 接受消息
         * @Prame:
         * 1.消费队列名称 QUEUE_NAME(hello)
         * 2.消费成功后是否自动应答(true)  手动应答(false)
         * 3.消费者成功消费回调
         * 4.消费者取消消费的回调
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */  //消息接受(消费)
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
