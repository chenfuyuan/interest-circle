package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("district")
public class City {
    private String name;
    private int id;
    private int parentId;
    private String code;
    private int order;
}
