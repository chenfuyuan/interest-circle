package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("district")
public class District {
    private String name;
    private int id;
    @TableField(exist = false)
    private District parent;
    private int parentId;
    private String code;
    private int order;
}

