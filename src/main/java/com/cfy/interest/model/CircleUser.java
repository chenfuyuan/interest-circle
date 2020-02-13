package com.cfy.interest.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle_user")
public class CircleUser {
    @TableId(type= IdType.AUTO)
    private long id;

    @TableField(exist = false)
    private User user;
    private long uid;

    @TableField(exist = false)
    private Circle circle;
    private int cid;


    private int type;

    private Date createTime;
    private Date updateTime;
    private int state;

    public static CircleUser build(long uid,int cid) {
        CircleUser circleUser = new CircleUser();
        circleUser.setUid(uid);
        circleUser.setCid(cid);
        circleUser.setType(0);
        circleUser.setState(1);
        return circleUser;
    }
}
