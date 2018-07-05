package com.neituiquan.work.controller;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.entity.SendMailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

@RestController
public class MailController {

    @Autowired
    JavaMailSender jms;

    @RequestMapping(method = RequestMethod.GET,path = "/sendCodeEmail")
    public AbsEntity sendCodeEmail(@RequestParam String mailPath,@RequestParam String code){
        AbsEntity absEntity = new AbsEntity();
        try {
            //建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            //发送者
            mainMessage.setFrom("nice_ohoh@163.com");
            //接收者
            mainMessage.setTo(mailPath);
            //发送的标题
            mainMessage.setSubject("内推圈-验证信息");
            //发送的内容
            mainMessage.setText(
                    "【内推圈】您的验证码是："+ code +"，用于手机验证，5分钟内有效。" +
                            "验证码提供给他人可能导致账号被盗，请勿泄露，谨防被骗。" +
                            "\n\n消息来自内推圈" +
                            "\n非常感谢的您的使用！" +
                            "\n使用过程中有任何问题，可以直接回复邮件，或者加QQ群722174383" +
                            "\nFrom:nice_ohoh@163.com");

            jms.send(mainMessage);
            absEntity.data = "邮件发送成功";
        }catch (Exception e){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.data = e.getMessage();
        }
        return absEntity;
    }


    @RequestMapping(method = RequestMethod.POST,path = "/sendMail")
    public AbsEntity sendMail(@RequestBody SendMailEntity entity){
        AbsEntity absEntity = new AbsEntity();
        try {
            //建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            //发送者
            mainMessage.setFrom("nice_ohoh@163.com");
            //接收者
            mainMessage.setTo(entity.getMailPath());
            //发送的标题
            mainMessage.setSubject("内推圈-"+entity.getTitle());
            //发送的内容
            mainMessage.setText(entity.getContent());

            jms.send(mainMessage);
            absEntity.data = "邮件发送成功";
        }catch (Exception e){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.data = e.getMessage();
        }
        return absEntity;
    }

}
