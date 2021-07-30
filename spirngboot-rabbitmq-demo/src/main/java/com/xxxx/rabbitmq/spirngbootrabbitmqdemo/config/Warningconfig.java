//package com.xxxx.rabbitmq.spirngbootrabbitmqdemo.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//
//public class Warningconfig {
//    //交换机名称
//    public static final String CONFIRM_EXCHANGE_NAME= "confirm_exchange";
//    //队列名称
//    public static final String CONFIRM_QUEUE_NAME= "confirm_queue";
//    //RoutingKey
//    public static final String CONFIRM_ROUTING_KEY= "key1";
//    //备份交换机
//    public static final String BACKUP_EXCHANGE_NAME= "backup_exchange";
//    //备份队列
//    public static final String BACKUP_QUEUE_NAME= "backup_queue";
//    //报警队列
//    public static final String WARNING_QUEUE_NAME= "warning_queue";
//
//
//    //声明备份交换机
//    @Bean("backupExchange")
//    public FanoutExchange backupExchange(){
//        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
//    }
//    //声明备份队列
//    @Bean("backupQueue")
//    public Queue backupQueue(){
//        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
//    }
//    //声明报警队列
//    @Bean("warningQueue")
//    public Queue warningQueue(){
//        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
//    }
//    //声明确认交换机
//    @Bean("confirmExchange")
//    public DirectExchange confirmExchange(){
//        //return new DirectExchange(CONFIRM_EXCHANGE_NAME);
//        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
//    }
//
//    //声明确认队列
//    @Bean("confirmQueue")
//    public Queue confirmQueue(){
//        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
//    }
//
//
//    //确认队列绑定确认交换机
//    @Bean
//    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmQueue") Queue confirmQueue,@Qualifier("confirmExchange")DirectExchange confirmExchange){
//        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
//    }
//
//    //备份队列绑定备份交换机
//    @Bean
//    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,@Qualifier("backupExchange")FanoutExchange backupExchange){
//        return BindingBuilder.bind(backupQueue).to(backupExchange);
//    }
//    //报警队列绑定备份交换机
//    @Bean
//    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,@Qualifier("backupExchange")FanoutExchange backupExchange){
//        return BindingBuilder.bind(warningQueue).to(backupExchange);
//    }
//
//
//
//}
