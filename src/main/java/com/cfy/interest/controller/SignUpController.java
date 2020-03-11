package com.cfy.interest.controller;

import com.cfy.interest.service.SignUpService;
import com.cfy.interest.vo.SendSmsMessage;
import com.cfy.interest.vo.SignUpMessage;
import com.cfy.interest.vo.SignUpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping("/signUp")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/signUp/authCode/send")
    @ResponseBody
    public SendSmsMessage getAuthCode(@RequestParam String phone) {
        SendSmsMessage sendSmsMessage;
        boolean phoneIsExist = signUpService.phoneIsExist(phone);
        if (phoneIsExist) {
            sendSmsMessage = new SendSmsMessage();
            //设置错误信息
            sendSmsMessage.setPhone(phone);
            sendSmsMessage.setMessage("该手机号已被注册");
        }else {
            //进行短信发送
            sendSmsMessage = signUpService.sendSms(phone);//传递手机进行短信发送
        }

        return sendSmsMessage;
    }

    @PostMapping("/signUp/check")
    @ResponseBody
    public SignUpMessage signUp(@RequestBody SignUpVo signUpVo) {
        String phone = signUpVo.getPhone();
        String name = signUpVo.getName();
        String password = signUpVo.getPassword();
        String authCode = signUpVo.getAuthCode();
        //新建注册信息
        SignUpMessage message = new SignUpMessage();
        message.setPhone(phone);
        //判断用户名是否存在
        if (signUpService.nameIsExist(name)) {
            message.setSuccess(false);
            message.setMessage("用户名已存在");
            return message;
        }
        //判断电话号码是否已存在
        if (signUpService.phoneIsExist(phone)) {
            message.setSuccess(false);
            message.setMessage("电话号码已注册");
            return message;
        }
        //判断验证码是否正确
        if (signUpService.checkAuthCode(phone,authCode)) {
            message.setMessage("成功注册");
            message.setSuccess(true);
            //保存用户
            signUpService.saveUser(signUpVo);
            return message;
        }else{
            message.setSuccess(false);
            message.setMessage("验证码错误");
        }
        return message;
    }



}
