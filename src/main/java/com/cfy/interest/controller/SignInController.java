package com.cfy.interest.controller;

import com.cfy.interest.model.User;
import com.cfy.interest.service.SignInService;
import com.cfy.interest.vo.SignInMessage;
import com.cfy.interest.vo.SignInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignInController {

    @Autowired
    private SignInService signInService;

    @RequestMapping("/signIn")
    public String signIn() {
        return "signin";
    }



    @PostMapping("/signIn/check")
    @ResponseBody
    public SignInMessage signIn(@RequestBody SignInVo signInVo, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(signInVo);
        SignInMessage message = signInService.checksignIn(signInVo);
        User user = message.getUser();
        //记住密码
        if (user == null) {
            return message;
        }
        if (signInVo.isRememberPassword()) {
            //记住密码
            //将用户的token放入cookie
            String token = user.getId()+":" + user.getToken();
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);
            message.setRememberPassword(true);
        }

        request.getSession().setAttribute("user",user);

        return message;
    }
}
