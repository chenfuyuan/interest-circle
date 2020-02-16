package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("circle_report")
public class CircleReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(exist = false)
    private User user;
    private Long uid;

    @TableField(exist = false)
    private Circle circle;
    private Integer cid;

    private Integer type;
    @TableField(exist = false)
    private String reportMessage;

    private Date createTime;
    private Date dealTime;

    private Integer state;

    public void setType(Integer type) {
        this.type = type;
        setReportMessage(type);
    }

    private void setReportMessage(Integer type) {
        switch (type) {
            case 1:
                reportMessage = "暴恐涉政";
                break;
            case 2:
                reportMessage = "色情内容";
                break;
            case 3:
                reportMessage = "违法犯罪";
                break;
            case 4:
                reportMessage = "谣言垃圾";
                break;
            case 5:
                reportMessage = "抄袭侵权";
                break;
            case 6:
                reportMessage = "其他";
                break;
        }
    }
}
