package com.cfy.interest.controller;


import com.cfy.interest.model.User;
import com.cfy.interest.service.UserService;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.ChangePasswordVo;
import com.cfy.interest.service.vo.SendSmsMessage;
import com.cfy.interest.service.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/user/info")
    public String userInfo() {
        return "user/info";
    }

    @PostMapping("/user/info/check")
    @ResponseBody
    public AjaxMessage infoChangeCheck(String userName, MultipartFile avatar, HttpServletRequest request) {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserName(userName);
        userInfoVo.setAvatar(avatar);
        log.info("userInfoVo = " + userInfoVo.toString());

        User user = (User) request.getSession().getAttribute("user");

        //检验用户名是否存在
        if (userService.nameExist(userInfoVo.getUserName(), user.getId())) {
            return new AjaxMessage(false, "用户名已存在");
        }

        //保存修改
        AjaxMessage message = userService.update(user, userInfoVo);

        return message;
    }

    /**
     * 跳转到更改密码页面
     * @return
     */
    @GetMapping("/user/password/change")
    public String changePasswordIndex() {
        return "user/changepassword";
    }

    /**
     * 更改密码页面 发送验证码
     * @param request
     * @return
     */
    @GetMapping("/user/password/authCode/send")
    @ResponseBody
    public SendSmsMessage changePasswordSendauthCode(HttpServletRequest request) {
        SendSmsMessage sendSmsMessage;
        User user = (User) request.getSession().getAttribute("user");
        String phone = user.getPhone();
        sendSmsMessage = userService.sendSms(phone);//传递手机进行短信发送
        return sendSmsMessage;
    }


    @PostMapping("/user/password/change/check")
    @ResponseBody
    public AjaxMessage changePasswordCheck(@RequestBody ChangePasswordVo changePasswordVo,HttpServletRequest request) {
        AjaxMessage message = new AjaxMessage();
        log.info("changePasswordVo = " + changePasswordVo);
        User user = (User) request.getSession().getAttribute("user");
        String phone = user.getPhone().trim();
        String authCode = changePasswordVo.getAuthCode().trim();
        String password = changePasswordVo.getPassword().trim();
        //检验验证码是否正确
        if (userService.checkAuthCode(phone, authCode)) {
            message.setMessage("修改成功");
            message.setSuccess(true);
            //修改密码
            log.info("修改密码");
            userService.changePassword(user,changePasswordVo);
            return message;
        }else{
            message.setSuccess(false);
            message.setMessage("验证码错误");
        }
        return message;
    }



}
