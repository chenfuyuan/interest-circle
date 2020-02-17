package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.mapper.UserOperationMessageMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.model.UserOperationMessage;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.UserService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ChangePasswordVo;
import com.cfy.interest.vo.SendSmsMessage;
import com.cfy.interest.vo.UserInfoVo;
import com.cfy.interest.utils.AuthCodeRandom;
import com.cfy.interest.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserOperationMessageMapper userOperationMessageMapper;

    @Autowired
    private AliyunOSSProvider aliyunOSSProvider;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public AjaxMessage update(User user, UserInfoVo userInfoVo) {
        AjaxMessage ajaxMessage = new AjaxMessage();
        long nowDate = new Date().getTime();
        String filePath = user.getAvatarPath();
        //上传图片
        if(userInfoVo.getAvatar()!=null) {
            filePath = uploadAvatar(userInfoVo.getAvatar());
            if (filePath.equals("") || filePath == null) {
                ajaxMessage.setMessage("图片上传失败");
                ajaxMessage.setSuccess(false);
                return ajaxMessage;
            }
        }
        user.setAvatarPath(filePath);
        user.setName(userInfoVo.getUserName());

        userMapper.updateById(user);

        //写入操作日志

        UserOperationMessage userOperationMessage = new UserOperationMessage();
        userOperationMessage.setMessage("更改用户信息 ");
        userOperationMessage.setUid(user.getId());
        userOperationMessage.setType(UserOperationMessage.UPDATE);
        userOperationMessageMapper.insert(userOperationMessage);

        return new AjaxMessage(true, "保存成功");
    }

    private String uploadAvatar(MultipartFile avatar) {
        aliyunOSSProvider.initOssClient();
        String fileUrl = aliyunOSSProvider.uploadImg2Oss(avatar);
        log.info("fileUrl = " + fileUrl);
        return fileUrl;
    }

    @Override
    public boolean nameExist(String name, long uid) {
        User user = userMapper.selectByName(name, uid);
        return user != null;
    }

    @Override
    public SendSmsMessage sendSms(String phone) {
        //用于传递信息
        log.info("向"+phone+"发送信息");
        SendSmsMessage sendSmsMessage = new SendSmsMessage();
        sendSmsMessage.setPhone(phone);

        //验证码是存在redis缓存中的
        String authCode = (String) redisTemplate.opsForValue().get(phone);
        //查看缓存中是否有该电话，有的话，继续用以前的验证码
        log.info(phone+"在缓存中的验证码为："+authCode);

        if (authCode == null) {
            log.info("缓存中未有"+phone+"的验证码");
            //获取6位随机数字
            authCode = AuthCodeRandom.getRandomNumberCode(6);
            //设置验证码
            redisTemplate.opsForValue().set(phone, authCode,5, TimeUnit.MINUTES);//将验证码存入缓存中,并设置过期时间为5分钟
            //用于测试 将验证码设置缓存为永久 方便测试
//            redisTemplate.opsForValue().set(phone, authCode);
            log.info("将" + authCode + "加入缓存");
        }
//        通过阿里云发送短信验证码
        sendSmsMessage.setAuthCode(authCode);
//        //调用将验证码和手机传递给阿里云短信进行短信发送
//        aliyunSmsProvider.sendSms(sendSmsMessage);

        //模拟发送阿里云短信，记得注释删除
        sendSmsMessage.setSuccess(true);
        sendSmsMessage.setMessage("短信发送成功");
        return sendSmsMessage;
    }

    /**
     * 根据传递过来的电话和验证码 检验验证码是否正确
     *
     * @param phone
     * @param authCode
     * @return
     */
    @Override
    public boolean checkAuthCode(String phone, String authCode) {
        log.info("phone = " + phone);
        log.info("authcode = " + authCode);
        log.info("phone.length() = " +phone.length());
        //检验
        if (phone == null || phone.length() != 11) {
            return false;
        }
        if (authCode == null || authCode.equals("")) {
            return false;
        }

        //从redis中获取验证码
        String phoneAuthCode = (String) redisTemplate.opsForValue().get(phone);
        if (phoneAuthCode == null) {
            log.info("未发送验证码");
            return false;
        }

        log.info(phone+"在redis中的验证码=" + phoneAuthCode);

        if (phoneAuthCode.equals(authCode)) {
            log.info("验证码输入正确");
            return true;
        } else {
            log.info("验证码输入错误");
            return false;
        }
    }

    /**
     * 根据传递过来的用户信息修改密码
     * @param user
     */
    @Override
    public void changePassword(User user, ChangePasswordVo changePasswordVo) {
        long nowDate = new Date().getTime();

        //给密码进行md5加密
        String md5Password = MD5Utils.MD5Encode(changePasswordVo.getPassword());
        //设置新的token
        String newToken = UUID.randomUUID().toString();
        user.setPassword(md5Password);
        //修改密码
        user.setToken(newToken);
        userMapper.updateById(user);

        log.info("修改密码成功");
        //记录日志
        UserOperationMessage operationMessage = new UserOperationMessage();
        operationMessage.setUid(user.getId());
        operationMessage.setMessage("更改密码");
        operationMessage.setType(UserOperationMessage.CHANGEPASSWORD);

        userOperationMessageMapper.insert(operationMessage);
        log.info("写入日志成功");
    }
}
