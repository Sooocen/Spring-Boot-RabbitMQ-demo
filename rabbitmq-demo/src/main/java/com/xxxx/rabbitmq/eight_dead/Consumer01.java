package com.xxxx.rabbitmq.eight_dead;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Socen
 * @Description:
 * 死信模式dead
 * 普通队列消费者 假死使消息转发到死信队列
 */
public class Consumer01 {
    //声明普通交换机、死信交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //声明两个队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    //接收消息
    public static void main(String[] args) throws Exception {
        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();
        //声明普通交换机、死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE,"direct");
        channel.exchangeDeclare(DEAD_EXCHANGE,"direct");

        //声明普通队列、死信队列(利用最后一项参数)
        //声明参数
        Map<String,Object> map= new HashMap<>();
        //过期时间 时间为毫秒(ms)  也可在发送消息时设置(常用) (在发送时设置)
        //map.put("x-message-ttl",10000);
        //设置死信交换机
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKey(lisi)
        map.put("x-dead-letter-routing-key","lisi");
        //设置队列最大长度
        map.put("x-max-length",100);
        /**
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,map);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //普通交换机、死信交换机 绑定路由Key
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收消息......");

        /**
         * 声明回调函数 deliverCallback接受消息  cancelCallback取消接受
         */
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody());
            if(msg.equals("这是第3条消息")){
                System.out.println("---------------------------");
                System.out.println("01接收到的消息为:"+new String(message.getBody())+ " 拒绝");
                System.out.println("---------------------------");
                //拒绝消息 false不放回普通队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("01接收到的消息为:" + new String(message.getBody()));
                System.out.println("接收队列为" + NORMAL_EXCHANGE + "   接收消息的路由Key为:" + message.getEnvelope().getRoutingKey());
                //false是否批量应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
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
         */ //开启手动应答 才能拒绝消息
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
    }

}
