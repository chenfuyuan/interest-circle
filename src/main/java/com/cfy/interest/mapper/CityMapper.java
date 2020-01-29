package com.cfy.interest.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.City;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CityMapper extends BaseMapper<City> {

    @Select("select * from district where parent_id = #{parentid}")
    List<City> findCityByProvince(int parentid);

}
