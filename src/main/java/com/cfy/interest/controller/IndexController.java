package com.cfy.interest.controller;

import com.cfy.interest.model.User;
import com.cfy.interest.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;


    @GetMapping("/")
    public String index(HttpServletRequest request) {
        System.out.println("index");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            Cookie[] cookies = request.getCookies();
            //遍历cookies
            if (cookies == null) {
                return "index";
            }
            for (Cookie cookie : cookies) {
                //找到名为token的cookie
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    System.out.println(token);
                    //查找用户
                    user = indexService.signInByToken(token);
                    System.out.println(user);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                }
            }
        }
        return "index";
    }

    @GetMapping("/logOut")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        //移除session中的user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        indexService.logOut(user.getId());
        return "index";
    }
}
