package com.cfy.interest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.provider.AliyunSmsProvider;
import com.cfy.interest.service.SignUpService;
import com.cfy.interest.service.vo.SendSmsMessage;
import com.cfy.interest.utils.AuthCodeRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceImpl implements SignUpService {


    @Autowired
    private AliyunSmsProvider aliyunSmsProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public SendSmsMessage sendSms(String phone) {
        RedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        //用于传递信息
        SendSmsMessage sendSmsMessage = new SendSmsMessage();
        sendSmsMessage.setPhone(phone);

        //验证码是存在redis缓存中的
        String authCode = (String) redisTemplate.opsForValue().get(phone);
        //查看缓存中是否有该电话，有的话，继续用以前的验证码
        System.out.println(authCode);

        if (authCode == null) {
            //获取6位随机数字
            authCode = AuthCodeRandom.getRandomNumberCode(6);
            //设置验证码
            redisTemplate.opsForValue().set(phone, authCode, 5 * 60);//将验证码存入缓存中
            System.out.println("将"+authCode+"加入缓存");
        }

        sendSmsMessage.setAuthCode(authCode);
        //调用将验证码和手机传递给阿里云短信进行短信发送
//        aliyunSmsProvider.sendSms(sendSmsMessage);

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
        if (user != null) {
            return true;
        }
        return false;
    }
}
