package com.cfy.interest.controller;

import com.cfy.interest.service.SignUpService;
import com.cfy.interest.service.vo.SendSmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;


    @GetMapping("/authCode")
    @ResponseBody
    public SendSmsMessage getAuthCode(@RequestParam String phone) {
        SendSmsMessage sendSmsMessage;
        boolean phoneIsExist = signUpService.PhoneIsExist(phone);
        if (phoneIsExist) {
            sendSmsMessage = new SendSmsMessage();
            //设置错误信息
            sendSmsMessage.setPhone(phone);
            sendSmsMessage.setMessage("该手机号已被注册");
        }
        //进行短信发送
        sendSmsMessage = signUpService.sendSms(phone);//传递手机进行短信发送


        return sendSmsMessage;
    }


}
