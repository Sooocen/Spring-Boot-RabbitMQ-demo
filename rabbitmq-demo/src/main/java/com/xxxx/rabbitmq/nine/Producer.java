package com.xxxx.rabbitmq.nine;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

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
        Map<String ,Object> map = new HashMap<>();
        map.put("x-max-priority",10);//官方0-255  建议0-10  先运行最大优先级为10 过大浪费内存
        channel.queueDeclare(QUEUE_NAME,true,false,false,map);

        //发送消息
        for (int i = 1; i < 11; i++) {
            String massage = "Hello RabbitMQ "+i;
            if( i == 5 ){
                //设置优先级
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,massage.getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, massage.getBytes());
            }
        }

        /**
         * @Description: 发送消息
         * @Prame:
         * 1.发送地址(交换机)
         * 2.路由Key(本次为队列名称)
         * 3.其他参数
         * 4.发送的消息内容(二进制传输)
         */

        System.out.println("消息已发送");




    }

}
