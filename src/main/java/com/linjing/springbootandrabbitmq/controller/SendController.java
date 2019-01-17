package com.linjing.springbootandrabbitmq.controller;

import com.linjing.springbootandrabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author cxc
 * @date 2019/1/15 18:01
 */
@RestController
public class SendController {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RequestMapping("send")
    public String send() {
        String context = "hello基本发送 " + new Date();
        System.out.println("Sender : " + context);
        //发送请求
        //赋予一个Id 用于确认消息回调
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.BASIC_QUEUE, (Object) context, correlationData);
        return "发送消息成功";
    }

    @RequestMapping("sendBasic")
    public String sendBasic() {
        String context = "point发送 " + new Date();
        System.out.println("Sender : " + context);
        //发送请求
        //赋予一个Id 用于确认消息回调
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, "point", context, correlationData);
        return "发送消息成功";
    }


    /**
     * 发送交换机请求
     *
     * @return
     */
    @RequestMapping("sendFanout")
    public String sendExchangeTest() {
        String context = "hello 发送交换机请求" + new Date();
        System.out.println("Sender : " + context);
        //发送请求
        //队列名称 ->路由队列名称 ->内容
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTINGKEY1, context);
        return "发送消息成功";
    }

    /**
     * 发送订阅请求
     *
     * @return
     */
    @RequestMapping("sendTopic")
    public String sendTopic() {
        String context = "hello 发送topic请求" + new Date();
        System.out.println("Sender : " + context);
        //发送请求
        //队列名称 ->路由队列名称 ->内容
        rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, "test.message", context);
        return "发送消息成功";
    }


}
