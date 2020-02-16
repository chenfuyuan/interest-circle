package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenfuyuan
 * @since 2020-02-14
 */
@Data
@TableName("article_operation_message")
public class ArticleOperationMessage implements Serializable {

    public static final int CREATE = 1;
    public static final int ESSENCE = 2;
    public static final int STICKY = 3;
    public static final int LIKE = 4;
    public static final int CANCELLIKE = 5;
    public static final int STAR = 6;
    public static final int CANCELSTAR = 7;
    public static final int COMMENT = 8;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private int id;
    private Long uid;

    private Integer aid;

    private String message;

    private Integer type;

    private LocalDateTime createTime;

    public void setType(Integer type) {
        this.type = type;
        switch (type) {
            case CREATE:setMessage("发布帖子");
                break;
            case ESSENCE:
                setMessage("加精帖子");
                break;
            case STICKY:
                setMessage("置顶帖子");
                break;
            case LIKE:
                setMessage("点赞帖子");
                break;
            case CANCELLIKE:
                setMessage("取消点赞帖子");
                break;
            case STAR:
                setMessage("收藏帖子");
                break;
            case CANCELSTAR:
                setMessage("取消收藏帖子");
                break;
            case COMMENT:
                setMessage("评论帖子");
                break;
        }
    }
}
