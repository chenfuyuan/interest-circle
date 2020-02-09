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
@TableName("user_operation_message")
public class UserOperationMessage {
    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(exist = false)
    private User user;

    private long uid;

    private String message;

    private long datetime;

    private int type;

    public static final int CREATE = 1;
    public static final int UPDATE = 2;
    public static final int CHANGEPASSWORD = 3;
}
