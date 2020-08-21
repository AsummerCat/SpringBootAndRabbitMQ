package com.linjing.springbootandrabbitmq;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class test1 {
    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    public void send(String msg){
        rabbitMessagingTemplate.convertAndSend("交换机","","消息体");
    }

}
