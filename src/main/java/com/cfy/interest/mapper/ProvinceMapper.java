package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Province;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ProvinceMapper extends BaseMapper<Province> {

    @Select("select * from district where parent_id = 0 order by order_ asc")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "citys",
                            column = "id",
                            javaType = List.class,
                            many = @Many(select = "com.cfy.interest.mapper.CityMapper.findCityByProvince")
                    )
            })
    public List<Province> getProvinces();

    @Select("select * from district where id = #{id} order by order_ asc")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "citys",
                            column = "id",
                            javaType = List.class,
                            many = @Many(select = "com.cfy.interest.mapper.CityMapper.findCityByProvince")
                    )
            }
    )
    public Province selectById(int id);
}
