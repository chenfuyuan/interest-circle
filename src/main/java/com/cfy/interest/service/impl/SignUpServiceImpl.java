package com.cfy.interest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.provider.AliyunSmsProvider;
import com.cfy.interest.service.SignUpService;
import com.cfy.interest.service.vo.SendSmsMessage;
import com.cfy.interest.utils.AuthCodeRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceImpl implements SignUpService {


    @Autowired
    private AliyunSmsProvider aliyunSmsProvider;

    @Autowired
    private UserMapper userMapper;

    @Override
    public SendSmsMessage sendSms(String phone) {
        //用于传递信息
        SendSmsMessage sendSmsMessage = new SendSmsMessage();
        sendSmsMessage.setPhone(phone);
        //获取6位随机数字
        String authCode = AuthCodeRandom.getRandomNumberCode(6);
        //设置验证码
        sendSmsMessage.setAuthCode(authCode);
        //调用将验证码和手机传递给阿里云短信进行短信发送
        aliyunSmsProvider.sendSms(sendSmsMessage);

        return sendSmsMessage;
    }

    @Override
    public boolean PhoneIsExist(String phone) {
        //设置查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);    //添加phone限制条件
        //根据查询条件用户
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
        if (user!=null) {
            return true;
        }
        return false;
    }
}
