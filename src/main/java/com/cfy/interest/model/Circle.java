package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("circle")
public class Circle {
    private int id;
    private String name;
    private String avatar_path;
    @TableField(exist = false)
    private District district;
    private int number;
    private User owner;
    private long create_time;
    private long update_time;
    private int state;
}
