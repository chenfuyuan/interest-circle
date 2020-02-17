package com.cfy.interest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过token查找user，进行登录
     *
     * @return
     */
    @Override
    public User signInByToken(String token) {
        String[] split = token.split(":");
        if (split.length != 2) {
            return null;
        }
        String id = split[0];
        String user_token = split[1];
        log.info("user_token = " + user_token);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("token", user_token);
        wrapper.eq("state", 1);
        User user = userMapper.selectOne(wrapper);
        return user;

    }

    @Override
    public void logOut(long id) {
        //更改用户状态为未登录
        String token = UUID.randomUUID().toString();
        userMapper.logOut(id,token);
    }
}
