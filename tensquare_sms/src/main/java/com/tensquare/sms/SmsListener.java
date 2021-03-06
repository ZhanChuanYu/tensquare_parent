package com.tensquare.sms;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * 用于接收消息的监听器
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    @RabbitHandler
    public void sendSms(Map<String,String> map){
        String mobile  = map.get("mobile");
        String code = map.get("code");
        System.out.println(mobile+"_"+code);
        try {
            smsUtil.sendSms(mobile, template_code, sign_name, "{\"code\":\""+code+"\"}");//{"code":"123456"}
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
