package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface UserMapper extends BaseMapper<User> {
    

    @Select("select * from user where phone = #{phone}")
    public User selectByPhone(String phone);

    @Update("update user set token = #{token} where id = #{id}")
    public void logOut(long id,String token);

    @Select("select * from user where name = #{name} and id !=#{uid}")
    public User selectByName(String name, long uid);

}
