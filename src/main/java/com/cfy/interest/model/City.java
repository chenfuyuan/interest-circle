package com.cfy.interest.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("district")
public class City {
    @TableId(type= IdType.AUTO)
    private int id;
    private String name;

    private int parentId;
    private String code;
    private int order;
}
