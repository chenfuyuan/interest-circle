package com.cfy.interest.service;

import com.cfy.interest.model.User;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.ChangePasswordVo;
import com.cfy.interest.service.vo.SendSmsMessage;
import com.cfy.interest.service.vo.UserInfoVo;

public interface UserService {

    public AjaxMessage update(User user, UserInfoVo userInfoVo);

    public boolean nameExist(String name,long uid);

   SendSmsMessage sendSms(String phone);

    boolean checkAuthCode(String phone, String authCode);

    void changePassword(User user, ChangePasswordVo changePasswordVo);
}