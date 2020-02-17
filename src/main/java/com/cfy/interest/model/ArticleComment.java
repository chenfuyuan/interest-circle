package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("article_comment")
public class ArticleComment {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(exist = false)
    private User user;
    private Long uid;

    @TableField(exist = false)
    private Article article;

    private Integer aid;

    @TableField(exist = false)
    private ArticleComment parent;

    private Integer rid;

    @TableField(exist = false)
    private List<ArticleComment> Replys;


    private String content;

    private Date createTime;
    private Date updateTime;


    private Integer likeNum;

    private Integer state;
}
