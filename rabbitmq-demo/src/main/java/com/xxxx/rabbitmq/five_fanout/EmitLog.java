package com.xxxx.rabbitmq.five_fanout;

import com.rabbitmq.client.Channel;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

import java.util.Scanner;

/**
 * @Author: Socen
 * @Description:
 * 发布订阅模式fanout
 * 生产者 一个发送消息 多个接收
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");


        //发送消息 获取控制台内容并发送
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * @Description: 发送消息
             * @Prame:
             * 1.发送地址(交换机)
             * 2.路由Key(本次为队列名称)
             * 3.其他参数 (消息持久化)
             * 4.发送的消息内容(二进制传输)  括号可定义字符编码(防止中文乱码)
             */ //设置生产发送消息为持久化发送消息(储存在磁盘中)
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者消息发送完成" + " 内容为:" + message);
        }

    }
}
