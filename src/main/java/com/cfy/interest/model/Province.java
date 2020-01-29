package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("district")
public class Province {
    private int id;
    private String name;
    @TableField(exist = false)
    private List<City> citys;
    private String code;
    private int order;
}
