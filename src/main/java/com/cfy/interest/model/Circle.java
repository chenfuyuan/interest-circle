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
    private Integer id;
    private String name;
    private String avatarPath;

    @TableField(exist = false)
    private District district;

    private Integer districtId;
    private Integer userNum;
    private Integer articleNum;

    @TableField(exist = false)
    private User owner;
    private Long ownerId;

    private Date createTime;
    private Date updateTime;
    private Integer state;

    //背景图片
    private String bgdPath;
    //介绍
    private String introduce;
}
