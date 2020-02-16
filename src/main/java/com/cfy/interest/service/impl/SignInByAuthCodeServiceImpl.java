package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.provider.AliyunSmsProvider;
import com.cfy.interest.service.SignInByAuthCodeService;
import com.cfy.interest.service.vo.SendSmsMessage;
import com.cfy.interest.service.vo.SignInByAuthCodeVo;
import com.cfy.interest.service.vo.SignInMessage;
import com.cfy.interest.utils.AuthCodeRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SignInByAuthCodeServiceImpl implements SignInByAuthCodeService {

    @Autowired
    private AliyunSmsProvider aliyunSmsProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 查询指定手机号是否存在
     *
     * @param phone 手机号码
     * @return
     */
    @Override
    public boolean phoneIsExist(String phone) {
        User user = userMapper.selectByPhone(phone);
        if (user != null) {
            System.out.println(user);
            return true;
        }
        return false;
    }

    /**
     * 给电话号码发送信息，并返回发送结果
     * @param phone
     * @return
     */
    @Override
    public SendSmsMessage SendSignInMessage(String phone) {
        //用于传递信息
        System.out.println("向"+phone+"发送信息");
        SendSmsMessage sendSmsMessage = new SendSmsMessage();
        sendSmsMessage.setPhone(phone);

        //验证码是存在redis缓存中的
        String authCode = (String) redisTemplate.opsForValue().get(phone+"SignIn");
        //查看缓存中是否有该电话，有的话，继续用以前的验证码
        System.out.println(phone+"在缓存中的验证码为："+authCode);

        if (authCode == null) {
            System.out.println("缓存中未有"+phone+"的验证码");
            //获取6位随机数字
            authCode = AuthCodeRandom.getRandomNumberCode(6);
            //设置验证码
            redisTemplate.opsForValue().set(phone+"SignIn", authCode,5, TimeUnit.MINUTES);//将验证码存入缓存中,并设置过期时间为5分钟
            //用于测试 将验证码设置缓存为永久 方便测试
//            redisTemplate.opsForValue().set(phone, authCode);
            System.out.println("将" + authCode + "加入缓存");
        }
//        通过阿里云发送短信验证码
        sendSmsMessage.setAuthCode(authCode);

        sendSmsMessage.setSuccess(true);
        sendSmsMessage.setMessage("短信发送成功");
//        //调用将验证码和手机传递给阿里云短信进行短信发送
        //aliyunSmsProvider.sendSms(sendSmsMessage);

        return sendSmsMessage;
    }

    /**
     * 1.从缓存中取出验证码 检验验证码是否正确
     * 2. 如果验证码正确，从数据库中通过手机号获取用户
     * 3. 填充登录信息
     *
     * @param signInByAuthCodeVo
     * @return
     */
    @Override
    public SignInMessage signInByAuthCode(SignInByAuthCodeVo signInByAuthCodeVo) {
        String phone = signInByAuthCodeVo.getPhone();
        String authCode = signInByAuthCodeVo.getAuthCode();
        SignInMessage message = new SignInMessage();

        //1.从缓存中取出验证码
        String authCodeInRedis = (String) redisTemplate.opsForValue().get(phone + "SignIn");
        //未取得数据，提示用户发送验证码
        if (authCodeInRedis == null) {
            message.setSuccess(false);
            message.setMessage("未发送验证码");
            return message;
        }
        //2.判断验证码是否正确
        if (authCodeInRedis.equals(authCode)) {
            //3.验证码正确获取用户
            User user = userMapper.selectByPhone(phone);
            //4.填充登录信息
            message.setSuccess(true);
            //修改登录状态
            user.setToken(UUID.randomUUID().toString());

            userMapper.updateById(user);

            message.setUser(user);
            //登录成功，清除缓存中的验证码信息
            redisTemplate.delete(phone + "SignIn");
        } else {
            message.setSuccess(false);
            message.setMessage("验证码错误");
        }
        return message;
    }

}
