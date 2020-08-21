package com.linjing.springbootandrabbitmq.controller;

import com.linjing.springbootandrabbitmq.RabbitConfig;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author cxc
 * @RabbitListener  bindings:绑定队列
 * @Queue 队列
 *        autoDelete:是不是一个临时队列
 * @exchange 指定交换机
 *     type: 指定一个交换机类型
 *     key: 指定路由键
 * @date 2019/1/15 20:37
 */
@Component
//标明当前类监听hello这个队列
@RabbitListener(bindings =@QueueBinding(
        value = @Queue(value = "${配置文件配置的queue}", autoDelete = "true"),
        exchange =@Exchange(value = "direct_exchange",type = ExchangeTypes.FANOUT),key = "路由键"))
public class ReceiveHello {
    //表示具体处理的方法
    @RabbitHandler
    public void receive(String name) {
        System.out.println("处理1" + name);
    }
}
