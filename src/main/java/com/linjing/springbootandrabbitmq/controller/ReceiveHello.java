package com.linjing.springbootandrabbitmq.controller;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author cxc
 * @date 2019/1/15 20:37
 */
@Component
//标明当前类监听hello这个队列
@RabbitListener(queues = "hello")
public class ReceiveHello {

    //表示具体处理的方法
    @RabbitHandler
    public void receive(String name) {
        System.out.println("处理" + name);
    }

}
