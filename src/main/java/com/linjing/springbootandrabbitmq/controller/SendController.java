package com.linjing.springbootandrabbitmq.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author cxc
 * @date 2019/1/15 18:01
 */
@RestController
public class SendController {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RequestMapping("send")
    public String send() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        //发送请求
        rabbitTemplate.convertAndSend("hello", context);
        return "发送消息成功";
    }
}
