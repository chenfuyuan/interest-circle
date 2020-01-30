package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.City;
import com.cfy.interest.model.District;
import com.cfy.interest.model.Province;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DistrictMapper extends BaseMapper<District> {

    @Select("select * from district where parent_id = #{parentid}")
    List<City> findCityByProvince(int parentid);

    @Select("select * from district where parent_id = 0 order by order_ asc")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "citys",
                            column = "id",
                            javaType = List.class,
                            many = @Many(select = "com.cfy.interest.mapper.DistrictMapper.findCityByProvince")
                    )
            })
    public List<Province> getProvinces();

    @Select("select * from district where id = #{id}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "citys",
                            column = "id",
                            javaType = List.class,
                            many = @Many(select = "com.cfy.interest.mapper.DistrictMapper.findCityByProvince")
                    )
            }
    )
    public Province selectProvinceById(int id);

    @Select("select * from district where id = #{id}")
    @Results(
            {
                    @Result(property = "parentId", column = "parent_id"),
                    @Result(property = "parent",
                            column = "parent_id",
                            one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById"))
            }
    )
    public District selectById(int id);

}
