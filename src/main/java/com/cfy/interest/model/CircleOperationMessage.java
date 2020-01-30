package com.cfy.interest.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作圈子信息记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle_operation_message")
public class CircleOperationMessage {
    private long id;
    private int cId;

    @TableField(exist = false)
    private Circle circle;

    private long datetime;
    private String message;
    private long uId;

    @TableField(exist = false)
    private User user;
    private int type;

}