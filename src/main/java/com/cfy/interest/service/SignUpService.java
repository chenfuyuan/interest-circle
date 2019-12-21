package com.cfy.interest.service;

import com.cfy.interest.service.vo.SendSmsMessage;
import org.springframework.stereotype.Service;

@Service
public interface SignUpService {

    /**
     * 发送短信 获取验证码 进行账号注册
     * @param phone
     * @return
     */
    public SendSmsMessage sendSms(String phone);

    /**
     * 判断手机是否注册过
     *
     * @param phone
     * @return
     */
    public boolean PhoneIsExist(String phone);



}
