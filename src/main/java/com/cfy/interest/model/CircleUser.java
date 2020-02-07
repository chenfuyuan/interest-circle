package com.cfy.interest.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle_user")
public class CircleUser {
    @TableId(type= IdType.AUTO)
    private long id;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Circle circle;

    private int type;
}
