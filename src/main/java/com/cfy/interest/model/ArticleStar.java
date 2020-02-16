package com.cfy.interest.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("article_star")
public class ArticleStar {

    @TableId(type = IdType.AUTO)
    private int id;

    private int uid;
    private int aid;

    private Date createTime;
    private Date updateTime;

    private int state;
}
