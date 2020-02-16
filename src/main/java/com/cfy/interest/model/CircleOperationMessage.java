package com.cfy.interest.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 操作圈子信息记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle_operation_message")
public class CircleOperationMessage {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Integer cId;

    @TableField(exist = false)
    private Circle circle;

    private Date datetime;
    private String message;
    private Long uId;

    @TableField(exist = false)
    private User user;
    private Integer type;

    public CircleOperationMessage build(Long uId,Integer cId) {
        CircleOperationMessage circleOperationMessage = new CircleOperationMessage();
        circleOperationMessage.setUId(uId);
        circleOperationMessage.setCId(cId);
        return circleOperationMessage;
    }

}
