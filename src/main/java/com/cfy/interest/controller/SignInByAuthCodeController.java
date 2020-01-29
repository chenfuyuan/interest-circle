package com.cfy.interest.controller;

import com.cfy.interest.model.User;
import com.cfy.interest.service.SignInByAuthCodeService;
import com.cfy.interest.service.vo.SendSmsMessage;
import com.cfy.interest.service.vo.SignInByAuthCodeVo;
import com.cfy.interest.service.vo.SignInMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignInByAuthCodeController {

    @Autowired
    private SignInByAuthCodeService service;

    @RequestMapping("/authCodeSignIn")
    public String authCodeSignIn() {
        return "authCodeSignIn";
    }

    /**
     * 从get中获取手机号码，检验手机号码是否存在，不存在提示用户注册，存在向该手机号发送验证码，并返回发送结果
     * @param phone
     * @return
     */
    @GetMapping("/sendSignInAuthCode")
    @ResponseBody
    public SendSmsMessage sendSignInAuthCode(@RequestParam String phone) {
        SendSmsMessage sendSmsMessage;
        boolean phoneIsExist = service.phoneIsExist(phone);
        //如果电话号码不存在提示用户
        if (!phoneIsExist) {
            sendSmsMessage = new SendSmsMessage();
            sendSmsMessage.setMessage("该手机号未注册");
            sendSmsMessage.setSuccess(false);
            sendSmsMessage.setPhone(phone);
        }else{
            //手机号存在，向该手机发送验证码
            sendSmsMessage = service.SendSignInMessage(phone);
        }
        System.out.println(sendSmsMessage);
        return sendSmsMessage;
    }

    @PostMapping("/signInByAuthCode")
    @ResponseBody
    public SignInMessage signInByAuthCode(@RequestBody SignInByAuthCodeVo signInByAuthCodeVo, HttpServletResponse response, HttpServletRequest request) {
        SignInMessage signInMessage = service.signInByAuthCode(signInByAuthCodeVo);
        User user = signInMessage.getUser();
        if (user == null) {
            return signInMessage;
        }

        //记住密码
        if (signInByAuthCodeVo.isRememberPassword()) {
            //记住密码
            //将用户的token放入cookie
            String token = user.getToken();
            response.addCookie(new Cookie("token", token));
            signInMessage.setRememberPassword(true);
        }

        request.getSession().setAttribute("user",user);




        return signInMessage;
    }
}
