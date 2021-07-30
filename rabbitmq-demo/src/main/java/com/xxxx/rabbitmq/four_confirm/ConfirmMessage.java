package com.xxxx.rabbitmq.four_confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.xxxx.rabbitmq.utils.RabbitMQUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: Socen
 * @Description:
 *    发布确认模式 获取运行时间比对(速度)择优
 *      1.单个确认
 *      2.批量确认
 *      3.异步确认
 */
public class ConfirmMessage {

    //批量发送消息数
    public static final int MESSAGE_COUNT = 1000;


    public static void main(String[] args) throws Exception {

        //执行单个确认 发布1000条单独确认消息  耗时804ms
        //ComfirmMessage.publishMessageIndividually();

        //执行批量确认 发布1000条批量确认消息  耗时159ms
        //ComfirmMessage.publishMessageBatch();

        //执行异步批量确认 发布1000条异步确认消息  耗时138ms
        ConfirmMessage.publishMessageAsync();

    }


    //单个确认
    public static void publishMessageIndividually() throws Exception {
        //获取队列
        Channel channel = RabbitMQUtil.getChannel();

        /**
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //记录开始时间
        Long Start = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            //单个消息发布确认
            Boolean result = channel.waitForConfirms();
            if( result  == true ) System.out.println("消息发送成功");
        }
        //记录结束时间
        Long End = System.currentTimeMillis();

        //打印发布用时
        System.out.println("发布"+MESSAGE_COUNT+"条单独确认消息  耗时"+(End-Start)+"ms");
    }

    //批量确认
    public static void publishMessageBatch() throws Exception{
        //获取队列
        Channel channel = RabbitMQUtil.getChannel();

        /**
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //记录开始时间
        Long Start = System.currentTimeMillis();
        //批量消息确认的大小
        int batchLength = 100;
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            //批量消息发布确认
            if((i+1)%batchLength == 0){
                Boolean result = channel.waitForConfirms();
                if( result  == true ) System.out.println("消息发送成功");
            }
        }
        //记录结束时间
        Long End = System.currentTimeMillis();

        //打印发布用时
        System.out.println("发布"+MESSAGE_COUNT+"条批量确认消息  耗时"+(End-Start)+"ms");
    }
    //异步确认
    public static void publishMessageAsync() throws Exception {
        //获取队列
        Channel channel = RabbitMQUtil.getChannel();

        /**
         * @Description: 声明队列
         * @Prame:
         * 1.队列名称 QUEUE_NAME(hello)
         * 2.队列内的消息是否需要持久化(true)  默认存储在内存中(false)
         * 3.队列内消息是否共享(多个消费者) true允许 false不允许
         * 4.是否自动删除(所有消费者断链后是否自动删除队列) true自动删除 false不自动删除
         * 5.6.~ 其他参数(延迟消息 死信消息)
         */
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的Hash表(适用于高并发)
         * 1.能轻松的将序号和消息进行关联
         * 2.能轻松的批量删除消息(可以适用需要删除)
         * 3.支持高并发(多线程)
         */ //外部确认的容器
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap = new ConcurrentSkipListMap();
        //记录开始时间
        Long Start = System.currentTimeMillis();


        /**
         * @Description: 声明回调函数 ackCallBack  nackCallBack
         * 成功回调函数 不成功回调函数
         * @Prame:
         * 1.消息的标记
         * 2.是否为批量确认
         */
        ConfirmCallback ackCallBack = (deliveryTag,multiple)->{
            //2.删除已经删除的消息
            if(multiple){
                ConcurrentNavigableMap confirmed = concurrentSkipListMap.headMap(deliveryTag);
                confirmed.clear();
            }else{
                concurrentSkipListMap.remove(deliveryTag);
            }
            System.out.println("确认的消息："+deliveryTag);
        };
        ConfirmCallback nackCallBack = (deliveryTag,multiple)->{
            //打印未确认的消息
            String message = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未确认的消息："+ message +"  未确认的消息的标记："+deliveryTag);
        };
        //消息监听器 (监听消息是否成功)
        channel.addConfirmListener(ackCallBack,nackCallBack);
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            //将已有的消息记录到外部容器中 (HashMap)
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
        }
        //记录结束时间
        Long End = System.currentTimeMillis();

        //打印发布用时
        System.out.println("发布"+MESSAGE_COUNT+"条异步确认消息  耗时"+(End-Start)+"ms");

    }
}
