package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle")
public class Circle implements Serializable {

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

    private long createTime;
    private long updateTime;
    private int state;
}
