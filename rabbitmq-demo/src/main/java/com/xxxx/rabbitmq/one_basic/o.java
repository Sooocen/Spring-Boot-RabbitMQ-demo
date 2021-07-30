package com.xxxx.rabbitmq.one_basic;

import com.rabbitmq.client.*;

public class o {

    //队列名称
    public static final String QUEUE_NAME="mirrior_hello";
    //交换机名称
    public static final String EXCHANGE_NAME="fed_exchange";

    //接受消息
    public static void main(String[] args) throws Exception {

        //创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");  //下游节点的IP
        //设置用户名 密码
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        //创建连接
        Connection connection = connectionFactory.newConnection();
        //获取通信信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");



        /**
         * 声明回调函数 deliverCallback  cancelCallback
         */
        //DeliverCallback deliverCallback = (consumerTag, message) ->{
        //    System.out.println(new String(message.getBody()));
        //};
        //CancelCallback cancelCallback = consumerTag->{
        //    System.out.println("消息消费被中断了喔");
        //};

        /**
         * @Description: 接受消息
         * @Prame:
         * 1.消费队列名称 QUEUE_NAME(hello)
         * 2.消费成功后是否自动应答(true)  手动应答(false)
         * 3.消费者成功消费回调
         * 4.消费者取消消费的回调
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */  //消息接受(消费)
        //channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
