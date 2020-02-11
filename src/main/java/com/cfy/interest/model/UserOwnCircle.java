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
public class UserOwnCircle {
    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(exist = false)
    private Circle circle;

    private int cid;

    private int type;
}
