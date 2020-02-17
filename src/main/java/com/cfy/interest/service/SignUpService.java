package com.cfy.interest.service;

import com.cfy.interest.vo.SendSmsMessage;
import com.cfy.interest.vo.SignUpVo;
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
    public boolean phoneIsExist(String phone);

    /**
     * 判断用户名是否注册
     * @param name
     * @return
     */
    public boolean nameIsExist(String name);

    /**
     * 根据传入的电话和验证码验证验证码是否正确
     * @param phone
     * @return
     */
    public boolean checkAuthCode(String phone,String authCode);


    /**
     * 保存用户信息入数据库
     * @param signUpVo
     * @return
     */
    public boolean saveUser(SignUpVo signUpVo);


}
