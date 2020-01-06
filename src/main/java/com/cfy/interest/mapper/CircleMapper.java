package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Circle;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface CircleMapper extends BaseMapper<Circle> {

    @Select("select * from circle where name = #{name}")
    @Results({
            @Result(property = "district",
                    column = "district_id",
                    one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById")
            ),
            @Result(property = "owner",
                    column = "owner_id",
                    one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")
            )
            })
    Circle findByName(String name);


}
