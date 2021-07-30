package com.xxxx.rabbitmq.one_basic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: Socen
 * @Description:
 * 生产者 轮询分发发送消息(公平分发)
 */
public class Producer {

    //队列名称
    public static final String QUEUE_NAME="hello";

    //发送消息
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
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发送消息
        String massage = "Hello RabbitMQ";
        /**
         * @Description: 发送消息
         * @Prame:
         * 1.发送地址(交换机)
         * 2.路由Key(本次为队列名称)
         * 3.其他参数
         * 4.发送的消息内容(二进制传输)
         */
        channel.basicPublish("",QUEUE_NAME,null,massage.getBytes());
        System.out.println("消息已发送");




    }

}
