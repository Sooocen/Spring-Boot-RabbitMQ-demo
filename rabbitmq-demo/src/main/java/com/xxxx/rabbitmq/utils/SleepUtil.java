package com.xxxx.rabbitmq.utils;

/**
 * 线程睡眠工具类
 */
public class SleepUtil {
    public static void sleep (int second){
        try{
            Thread.sleep(second * 1000 );
        } catch(InterruptedException interruptedException){
            Thread.currentThread().interrupt();
        }

    }
}
