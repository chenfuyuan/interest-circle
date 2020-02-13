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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private int id;
    private Long uid;

    private Integer aid;

    private String message;

    private Integer type;

    private LocalDateTime createTime;


}
