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
    private Integer id;

    private Long uid;
    private Integer aid;

    private Date createTime;
    private Date updateTime;

    private Integer state;

    public ArticleStar(Long uid,Integer aid) {
        this.uid = uid;
        this.aid = aid;
    }
}
