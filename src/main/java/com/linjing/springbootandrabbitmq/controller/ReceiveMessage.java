package com.linjing.springbootandrabbitmq.controller;

import com.linjing.springbootandrabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author cxc
 * @date 2019/1/15 20:37
 */
@Component
public class ReceiveMessage {

    /**
     * fanout交换机投递消息 因为有监听了两个队列  然后 两个队列又是绑定的同一个路由 所以 会执行两次
     *
     * @param name
     */
    @RabbitListener(queues = {"firstQueue", "secondQueue"})
    public void receiveFirstQueue(String name) {
        System.out.println("fanout交换机分配请求" + name);
    }


    /**
     * 匹配请求
     * topic
     */
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    public void receiveTopicQueue(String name) {
        System.out.println("topic1交换机分配请求" + name);
    }

    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE2)
    public void receiveTopicQueue2(String name) {
        System.out.println("topic2交换机分配请求" + name);
    }


    /**
     * 消息回调确认
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    //@RabbitListener(queues = RabbitConfig.BASIC_QUEUE)
    //public void process(Message message, Channel channel) throws IOException {
    //    // 采用手动应答模式, 手动确认应答更为安全稳定
    //    System.out.println("客户端接收到请求");
    //    try {
    //        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
    //        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    //        System.out.println("receive: " + new String(message.getBody()));
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        //丢弃这条消息
    //        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    //        System.out.println("receiver fail");
    //    }
    //}


    ///**
    // * 事务模式
    // */
    //@RabbitListener(queues = RabbitConfig.BASIC_QUEUE)
    //public void process(Message message, Channel channel) throws IOException {
    //    // 采用手动应答模式, 手动确认应答更为安全稳定
    //    System.out.println("客户端接收到请求");
    //        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
    //        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    //        System.out.println("receive: " + new String(message.getBody()));
    //        //模拟异常 回滚
    //        int i=1/0;
    //}
}

