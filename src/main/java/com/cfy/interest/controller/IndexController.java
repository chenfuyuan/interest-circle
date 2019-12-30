package com.cfy.interest.controller;

import com.cfy.interest.model.User;
import com.cfy.interest.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;


    @GetMapping("/")
    public String index(HttpServletRequest request) {
        System.out.println("index");
        Cookie[] cookies = request.getCookies();
        //遍历cookies
        for (Cookie cookie : cookies) {
            //找到名为token的cookie
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                System.out.println(token);
                //查找用户
                User user = indexService.signInByToken(token);
                System.out.println(user);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
            }
        }
        return "index";
    }
}
