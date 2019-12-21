package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    private long id;
    private String name;
    private String phone;
    private String password;
    private String sex;
    private long create_time;
    private long update_time;
}
