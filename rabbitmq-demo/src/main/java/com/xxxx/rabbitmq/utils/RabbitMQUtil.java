package com.xxxx.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接工厂创建信道
 */
public class RabbitMQUtil {

    //得到一个连接得Channel
    public static Channel getChannel() throws Exception {
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

        return channel;
    }
}
