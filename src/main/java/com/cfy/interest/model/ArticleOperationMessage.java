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
    public static final int CANCELSTICKY = 9;
    public static final int CANCELESSENCE = 10;
    public static final int DELETE = 11;
    public static final int REPORT = 12;
    public static final int DEALREPORT = 13;
    public static final int ACOMMENT = 14;
    public static final int REPLY = 15;
    public static final int DELETECOMMENT = 16;
    public static final int DELETEREPLY = 17;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long uid;

    private Integer aid;

    private String message;

    private Integer type;

    private LocalDateTime createTime;

    public ArticleOperationMessage (Long uid,Integer aid,Integer type) {
        this.uid = uid;
        this.aid = aid;
        setType(type);
    }
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
            case CANCELSTICKY:
                setMessage("取消置顶帖子");
                break;
            case CANCELESSENCE:
                setMessage("取消加精帖子");
                break;
            case DELETE:
                setMessage("删除帖子");
                break;
            case REPORT:
                setMessage("举报帖子");
                break;
            case DEALREPORT:
                setMessage("处理举报帖子");
                break;
            case ACOMMENT:
                setMessage("评论帖子");
                break;
            case REPLY:
                setMessage("回复评论");
                break;
            case DELETECOMMENT:
                setMessage("删除评论");
                break;
            case DELETEREPLY:
                setMessage("删除回复");
                break;
        }
    }
}
