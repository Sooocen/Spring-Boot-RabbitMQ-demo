package com.xxxx.rabbitmq.eight_dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

import java.util.Scanner;

/**
 * @Author: Socen
 * @Description:
 * 死信模式 dead
 * 生产者 发送消息
 */
public class Producer {

    //声明普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //声明普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";

    //发送死信消息(即设置TTL值)
    public static void main(String[] args) throws Exception {
        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        //设置过期时间 单位毫秒(ms)
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("30000").build();
        for (int i = 1; i < 11 ; i++) {
            String message = "这是第"+i+"条消息";
            /**
             * @Description: 发送消息
             * @Prame:
             * 1.发送地址(交换机)
             * 2.路由Key(本次为队列名称)
             * 3.其他参数 (消息持久化)
             * 4.发送的消息内容(二进制传输)  括号可定义字符编码(防止中文乱码)
             */ //设置生产发送消息为持久化发送消息(储存在磁盘中)
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes("UTF-8"));
            System.out.println("生产者消息发送完成" + " 内容为:" + message);

        }


    }
}
