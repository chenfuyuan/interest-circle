package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.District;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DistrictMapper extends BaseMapper<District> {

    @Select("select * from district where parent_id = 0")
    List<District> getProvinces();

    @Select("select * from district where parent_id = #{parentid}")
    @Results(
            @Result(property = "parent",
                    column = "parent_id",
                    one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById")
    ))
    List<District> findCityByProvince(int parentid);
}

