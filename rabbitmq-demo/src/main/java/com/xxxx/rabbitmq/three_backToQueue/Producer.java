package com.xxxx.rabbitmq.three_backToQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

import java.util.Scanner;

/**
 * @Author: Socen
 * @Description:
 * 生产者 轮询分发发送消息
 * 手动确认  某一消费者突然断链时重新入队
 */
public class Producer  {

    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {

        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        //开启发布确认
        channel.confirmSelect();

        /**
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */  //持久化队列设置
        Boolean Durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME,Durable,false,false,null);

        //发送消息 获取控制台内容并发送
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message =scanner.next();
            /**
             * @Description: 发送消息
             * @Prame:
             * 1.发送地址(交换机)
             * 2.路由Key(本次为队列名称)
             * 3.其他参数 (消息持久化)
             * 4.发送的消息内容(二进制传输)  括号可定义字符编码(防止中文乱码)
             */ //设置生产发送消息为持久化发送消息(储存在磁盘中)
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("消息发送完成"+" 内容为:"+message);
        }
    }
}
