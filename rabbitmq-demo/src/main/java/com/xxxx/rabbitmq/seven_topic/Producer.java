package com.xxxx.rabbitmq.seven_topic;

import com.rabbitmq.client.Channel;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;
import jdk.nashorn.internal.runtime.ECMAException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Socen
 * @Description:
 * 主题模式 topic
 * 生产者 发送消息 消费者模糊匹配接收
 */
public class Producer {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        //利用Util获取信道
        Channel channel = RabbitMQUtil.getChannel();

        //发送消息
        Map<String,String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put( "quick.orange.rabbit" , "被队列Q1,Q2接收到" );
        bindingKeyMap.put( "lazy.orange.elephant" , "被队列Q1,Q2接收到" );
        bindingKeyMap.put( "quick.orange.fox" , "被队列Q1接收到" );
        bindingKeyMap.put( "lazy.brown.fox" , "被队列Q2接收到" );
        bindingKeyMap.put( "lazy.pink.rabbit" , "满足两个绑定但只被Q2接收一次" );
        bindingKeyMap.put( "quick.brown.fox" , "不匹配任何队列 被丢弃" );
        bindingKeyMap.put( "quick.orange.male.rabbit" , "是四个单词不匹配任何队列 被丢弃" );
        bindingKeyMap.put( "lazy.orange.male.rabbit" , "是四个单词但匹配Q2" );
        //高级for循环 iter
        for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
            String rootingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME,rootingKey,null,message.getBytes("UTF-8"));
            System.out.println("生产者消息发送完成" + " 内容为:" + message);
        }

    }
}
