package com.cfy.interest.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.service.SignInService;
import com.cfy.interest.service.vo.SignInMessage;
import com.cfy.interest.service.vo.SignInVo;
import com.cfy.interest.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl implements SignInService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public SignInMessage checksignIn(SignInVo signInVo) {
        String md5Password = MD5Utils.MD5Encode(signInVo.getPassword());
        String phone = signInVo.getPhone();

        //设置查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.and(i->{
            i.eq("phone", phone).eq("password",md5Password);
        });

        //数据库查询user
        User user = userMapper.selectOne(wrapper);

        //封装登录信息
        SignInMessage message = new SignInMessage();

        if (user != null) {
            message.setMessage("登录成功");
            message.setSuccess(true);
            message.setUser(user);
        } else {
            message.setMessage("用户名密码错误");
            message.setSuccess(false);
        }

        return message;
    }

    @Override
    public boolean rememberPassword(SignInMessage signInMessage) {
        User user = signInMessage.getUser();
        
        return false;
    }
    
}