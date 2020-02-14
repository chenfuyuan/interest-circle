package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("article_sticky")
@Data
public class ArticleSticky {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer cid;
    private Integer aid;

    @TableField(exist = false)
    private Article article;
    private Date CreateTime;
    private Date UpdateTime;
    private Integer state;

}
