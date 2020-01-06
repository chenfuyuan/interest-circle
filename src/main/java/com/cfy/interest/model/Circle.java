package com.cfy.interest.model;

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
    private String portrait_path;
    private District district;
    private int number;
    private User owner;
    private long create_time;
    private long update_time;
}
