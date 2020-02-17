package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("article_comment_reply")
public class ArticleCommentReply {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(exist = false)
    private User user;
    private Long uid;

    @TableField(exist = false)
    private User replyUser;
    private Long ruid;


    private String content;

    private Date createTime;
    private Date updateTime;


    @TableField(exist = false)
    private ArticleComment articleComment;
    private Integer acid;
    private Integer likeNum;

    private Integer state;


    private Integer type;
}
