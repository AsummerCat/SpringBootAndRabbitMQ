package com.linjing.springbootandrabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitConfig {
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 基本消息发送
     */
    public static final String BASIC_QUEUE = "basic_queue";
    /**
     * Direct模式的交换机
     */
    public static final String DIRECT_EXCHANGE = "direct_exchange";


    /**
     * 消息交换机的名字
     */
    public static final String EXCHANGE = "exchangeTest";
    /**
     * 队列key1
     */
    public static final String ROUTINGKEY1 = "queue_one_key1";
    /**
     * 队列key2
     */
    public static final String ROUTINGKEY2 = "queue_one_key2";


    /**
     * TOPIC模式
     */
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";


    /**
     * *********************************************************************************************
     *  ********************************Direct模式普通投递************************************
     *   *********************************************************************************************
     */

    /**
     * 基本点对点 消息发送
     */
    @Bean
    public Queue helloQueue() {
        return new Queue(BASIC_QUEUE);
    }

    /**
     * 创建Direct模式交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directBasicExchange() {
        Map<String, Object> arguments = new HashMap<>();
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding binding_basic() {
        return BindingBuilder.bind(helloQueue()).to(directBasicExchange()).with("point");
    }


    /**
     * ***********************************************************************************************
     *  **************************************Fanout模式交换机投递*************************************************
     *   *********************************************************************************************
     */
    /**
     * Fanout模式
     * 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配/**
     *
     * @return * 交换机投递
     */
    @Bean
    public DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE, true, false);
        return directExchange;
    }

    @Bean
    public Queue firstQueue() {
        return new Queue("firstQueue");
    }

    @Bean
    public Queue secondQueue() {
        return new Queue("secondQueue");
    }

    /**
     * 将消息队列1和交换机进行绑定
     */
    @Bean
    public Binding binding_one() {
        return BindingBuilder.bind(firstQueue()).to(directExchange()).with(ROUTINGKEY1);
    }

    /**
     * 将消息队列2和交换机进行绑定
     */
    @Bean
    public Binding binding_two() {
        return BindingBuilder.bind(secondQueue()).to(directExchange()).with(ROUTINGKEY1);
    }

    /**
     * ***********************************************************************************************
     * **************************************Topic模式 投递*************************************************
     * *********************************************************************************************
     */


    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2);
    }

    /**
     * 创建top模式交换机
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("test.message");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("test.#");
    }

    /** ======================== 定制一些处理策略 =============================*/

    /**
     * 定制化amqp模版 发布确认模式
     * <p>
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        RabbitTemplate rabbitTemp = new RabbitTemplate(connectionFactory);
        // 消息发送失败返回到队列中, yml需要配置 publisher-returns: true
        rabbitTemp.setMandatory(true);

        // 消费者ACK消息返回, yml需要配置 publisher-returns: true
        rabbitTemp.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().toString();
            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });

        // 生产者发送消息确认, yml需要配置 publisher-confirms: true
        rabbitTemp.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.info("消息发送到exchange失败,原因: {}", cause);
            }
        });
//        rabbitTemp.setMessageConverter(new MessageConverter() {
//           //发送消息要转换的  生产者重写
//            @Override
//            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
//               //指定输出格式
//                messageProperties.setContentType("text/xml");
//                messageProperties.setContentEncoding("UTF-8");
//                Message message=new Message(JSON.toJSONBytes(o),messageProperties);
//                System.out.println("调用了消息解析器");
//                return null;
//            }
//
//            //接收消息要转换的  消费者者重写
//            @Override
//            public Object fromMessage(Message message) throws MessageConversionException {
//              //使用json解析
//                System.out.println(new String(message.getBody(),"UTF-8"));
//                return null;
//            }
//        });
        return rabbitTemp;
    }

//    /**
//     * 定制化amqp模版 事务模式
//     */
//    @Bean
//    //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public RabbitTemplate rabbitTemplate() {
//        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
//        RabbitTemplate rabbitTemp = new RabbitTemplate(connectionFactory);
//        // 消息发送失败返回到队列中, yml需要配置 publisher-returns: true
//        rabbitTemp.setMandatory(true);
//        //开启事务
//        rabbitTemp.setChannelTransacted(true);
//        return rabbitTemp;
//    }
}