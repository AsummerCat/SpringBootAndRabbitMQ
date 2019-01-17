package com.linjing.springbootandrabbitmq.controller;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列之类的测试
 * 主要调试一下 queue的当初
 *
 * @author cxc
 * @date 2019/1/17 14:14
 */
@Configuration
public class ArgumentsMessage {
    /**
     * 首先创建一个队列
     */
    @Bean
    public Queue argumentsQueue() {
        Map<String, Object> arguments = new HashMap<>();
        //设置消息过期时间
        //arguments.put("x-message-ttl", 500);
        //队列无访问指定时间过期
        //arguments.put("x-expires",10000);
        //队列最大长度
        //arguments.put("x-max-length",100);
        ////队列最大占用空间
        //arguments.put("x-max-length-bytes",10000);
        ////队列长度超出最大 或者过期等~ 推送到指定的交换机去
        // arguments.put("x-dead-letter-exchange", "timeoutExchange");
        ////队列删除 投递给死信交换机
        //arguments.put("x-dead-letter-routing-key", "dieExchange");
        //优先级队列
        //arguments.put("x-max-priority",5);
        //路由失败 备用队列
        arguments.put("alternate-exchange","alternateExchange");

        return new Queue("ArgumentsQueue", false, false, false, arguments);
    }

    /**
     * 一个交换器
     */
    @Bean
    public DirectExchange argumentsExchange() {
        DirectExchange directExchange = new DirectExchange("ArgumentsExchange", true, false);
        return directExchange;
    }

    /**
     * 队列绑定到交换器上
     */
    @Bean
    public Binding bindingArguments() {
        return BindingBuilder.bind(argumentsQueue()).to(argumentsExchange()).with("Arguments");
    }

    /**
     * 超时交换器
     */
    @Bean
    public DirectExchange timeoutExchange() {
        DirectExchange directExchange = new DirectExchange("timeoutExchange", true, false);
        return directExchange;
    }

    @Bean
    public Binding bindingTimeout() {
        return BindingBuilder.bind(timeoutQueue()).to(argumentsExchange()).with("Arguments");
    }

    @Bean
    public Queue timeoutQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",3);
        return new Queue("timeoutQueue",true,false,false,arguments);
    }

    /**
     * 死信队列
     */
    @Bean
    public DirectExchange dieExchange() {
        DirectExchange directExchange = new DirectExchange("dieExchange", true, false);
        return directExchange;
    }

    @Bean
    public Binding bindingdie() {
        return BindingBuilder.bind(dieQueue()).to(argumentsExchange()).with("Arguments");
    }

    @Bean
    public Queue dieQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",1);
        return new Queue("dieQueue",true,false,false,arguments);
    }

    /**
     * 路由失败 备用队列
     */

    @Bean
    public DirectExchange alternateExchange() {
        DirectExchange directExchange = new DirectExchange("alternateExchange", true, false);
        return directExchange;
    }

    @Bean
    public Binding bindingAlternate() {
        return BindingBuilder.bind(AlternateQueue()).to(argumentsExchange()).with("dddd");
    }

    @Bean
    public Queue AlternateQueue() {
        Map<String, Object> arguments = new HashMap<>();
        return new Queue("alternateQueue",true,false,false,arguments);
    }

}
