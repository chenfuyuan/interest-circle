package com.cfy.interest.service;


import com.cfy.interest.vo.SendSmsMessage;
import com.cfy.interest.vo.SignInByAuthCodeVo;
import com.cfy.interest.vo.SignInMessage;

public interface SignInByAuthCodeService {

    /**
     * 检验手机号是否存在
     */
    boolean phoneIsExist(String phone);

    /**
     * 向手机号发送短信验证码进行登录
     */
    SendSmsMessage SendSignInMessage(String phone);


    /**
     * 通过验证码登录账号，并获取账号信息
     * @return
     */
    SignInMessage signInByAuthCode(SignInByAuthCodeVo signInByAuthCodeVo);

}
