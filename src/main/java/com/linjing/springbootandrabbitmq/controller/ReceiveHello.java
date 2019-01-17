//package com.linjing.springbootandrabbitmq.controller;
//
//import com.linjing.springbootandrabbitmq.RabbitConfig;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author cxc
// * @date 2019/1/15 20:37
// */
//@Component
////标明当前类监听hello这个队列
//@RabbitListener(queues = RabbitConfig.BASIC_QUEUE)
//public class ReceiveHello {
//
//    //表示具体处理的方法
//    @RabbitHandler
//    public void receive(String name) {
//        System.out.println("处理1" + name);
//    }
//
//}
