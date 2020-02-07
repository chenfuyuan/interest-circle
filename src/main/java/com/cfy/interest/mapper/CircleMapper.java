package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Circle;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CircleMapper extends BaseMapper<Circle> {

    @Select("select * from circle where name = #{name}")
    @Results({
            @Result(property = "districtId",column = "district_id"),
            @Result(property = "district",
                    column = "district_id",
                    one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById")
            ),
            @Result(property = "ownerId",column = "owner_id"),
            @Result(property = "owner",
                    column = "owner_id",
                    one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")
            )
            })
    Circle findByName(String name);


    /**
     * 查询所有圈子
     * @return
     */
    @Select("select * from circle where state !=-1")
    @Results({
            @Result(property = "districtId",column = "district_id"),
            @Result(property = "district",
                    column = "district_id",
                    one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById")
            ),
            @Result(property = "ownerId",column = "owner_id"),
            @Result(property = "owner",
                    column = "owner_id",
                    one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")
            )
    })
    List<Circle> selectAll();


    @Select("select * from circle where state != -1 and (district_id = #{districtId} or district_id in (" +
            "select id from district where parent_id = #{districtId}))")
    @Results({
            @Result(property = "districtId",column = "district_id"),
            @Result(property = "district",
                    column = "district_id",
                    one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById")
            ),
            @Result(property = "ownerId",column = "owner_id"),
            @Result(property = "owner",
                    column = "owner_id",
                    one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")
            )
    })
    List<Circle> selectAllByDistrict(Integer districtId);


}
