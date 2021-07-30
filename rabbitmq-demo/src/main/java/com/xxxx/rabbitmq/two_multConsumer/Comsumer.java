package com.xxxx.rabbitmq.two_multConsumer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

/**
 * 消费者 多消息分发
 * 配置开启多线程运行(并行运行)
 * 创建多个消费者 测试
 */
public class Comsumer {
    //队列名称
    public static final String QUEUE_NAME="hello";

    //接受消息
    public static void main(String[] args) throws Exception {

        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();

        /**
         * 声明回调函数 deliverCallback  cancelCallback
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("线程一接收到的消息为:"+new String(message.getBody()));
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
         */  //消息接受(消费)
        System.out.println("工作线程二运行中 等待接受消息 - - - ");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
