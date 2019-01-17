package com.linjing.springbootandrabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 事务发送
 *
 * @author cxc
 * @date 2019/1/15 18:01
 */
@RestController
public class TxController {
    @Autowired
    private TransactionSender2 transactionSender2;


    @RequestMapping("sendTx")
    public String sendTx() {
        String context = "事务基本发送 " + new Date();
        System.out.println("Sender : " + context);
        //发送请求
        transactionSender2.send(context);
        return "发送消息成功";
    }
}
