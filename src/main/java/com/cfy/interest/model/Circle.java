package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle")
public class Circle implements Serializable {

    @TableId(type= IdType.AUTO)
    private int id;
    private String name;
    private String avatarPath;

    @TableField(exist = false)
    private District district;

    private int districtId;
    private int userNum;
    private int articleNum;

    @TableField(exist = false)
    private User owner;
    private long ownerId;

    private Date createTime;
    private Date updateTime;
    private int state;

    //背景图片
    private String bgdPath;
    //介绍
    private String introduce;
}
