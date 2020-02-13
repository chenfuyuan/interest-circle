package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Circle;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CircleMapper extends BaseMapper<Circle> {

    @Results( id="circleMap",value = {
            @Result(property = "districtId", column = "district_id"),
            @Result(property = "district",
                    column = "district_id",
                    one = @One(select = "com.cfy.interest.mapper.DistrictMapper.selectById")
            ),
            @Result(property = "ownerId", column = "owner_id"),
            @Result(property = "owner",
                    column = "owner_id",
                    one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")
            )
    })
    @Select("select * from circle where name = #{name}")
    Circle findByName(String name);


    /**
     * 查询所有圈子
     *
     * @return
     */
    @Select("select * from circle where state !=-1 and id not in (select cid from circle_user where uid = #{uid})")
    @ResultMap("circleMap")
    List<Circle> selectAll(long uid);


    @Select("select * from circle where state != -1 and id not in (select cid from circle_user where uid = #{uid}) " +
            "and(district_id = #{districtId} or district_id in (" +
            "select id from district where parent_id = #{districtId}))")
    @ResultMap("circleMap")
    List<Circle> selectAllByDistrict(Integer districtId, long uid);


    @Select("select * from circle where id = #{id}")
    @ResultMap("circleMap")
    Circle selectById(Integer id);

    @Update("update circle set user_num = user_num + 1 where id = #{id}")
    void joinMember(int id);

    @Update("update circle set user_num=user_num -1 where id = #{id}")
    void quitMember(int id);
}
