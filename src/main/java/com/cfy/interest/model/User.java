package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type= IdType.AUTO)
    private long id;
    private String name;
    private String phone;
    private String password;
    private String sex;
    private String token;
    private long createTime;
    private long updateTime;
    private int state;
    private String avatarPath;



}
