package com.cfy.interest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("/signup")
    public String signup() {

        return "signup";
    }

    @GetMapping("/signin")
    public String signin(@RequestParam(name = "username", required = false) String username, Model model) {
        System.out.println(username);
        model.addAttribute("username", username);
        System.out.println(model.getAttribute("username"));
        return "signin";
    }

    public String authCode() {

        return "hello";
    }
}
