package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName("article")
@NoArgsConstructor
public class Article implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private int id;

    private String content;

    private Long uid;

    private Integer cid;

    private Integer type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 观看数
     */
    private Integer watchNum;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 收藏数
     */
    private Integer starNum;

    public Article(long uid, int cid, String content) {
        this.uid = uid;
        this.cid = cid;
        this.content = content;
        type = 1;
        state = 1;
        commentNum = 0;
        watchNum = 0;
        likeNum = 0;
        starNum = 0;
    }

}
