package com.xxxx.rabbitmq.three_backToQueue;

import com.rabbitmq.client.*;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;
import com.xxxx.rabbitmq.utils.SleepUtil;

/**
 * @Author: Socen
 * @Description:
 * 消费者 接收消息
 * 手动确认
 * 某一消费者突然断链(未确认) 消息重新入队
 */
public class Consumer02 {

    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {

        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("消费者02正在等待接受消息(长时间处理)");


        /**
         * 声明回调函数 deliverCallback  cancelCallback
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            //沉睡30S(三十秒钟)
            SleepUtil.sleep(30);
            System.out.println("接受到的消息为:"+new String(message.getBody(),"UTF-8"));
            /**
             * @Description: 手动应答
             * @Prame:
             * 1.消息标记 Tag
             * 2.是否批量应答 批量应答(true)  不批量应答(false)
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = consumerTag->{
            System.out.println(consumerTag + "消息取消消费了喔");
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
        //采用手动应答
        Boolean AutoAck = false;
        /**
         * @Description: 设置不公平分发
         * @Prame:
         * 1.是否为不公平分发模式公平分发(0)  不公平分发(1)
         */ //设置公平分发
        //int ProfetchCount = 1 ;
        // 设置预取值  ProfetchCount不为1
        int ProfetchCount = 5 ;
        channel.basicQos(ProfetchCount);
        channel.basicConsume(TASK_QUEUE_NAME,AutoAck,deliverCallback,cancelCallback);


    }
}
