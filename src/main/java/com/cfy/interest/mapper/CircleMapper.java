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
    @Select("select * from circle where state =1  and id not in (select cid from circle_user where uid = #{uid} and " +
            "state!=0)")
    @ResultMap("circleMap")
    List<Circle> selectAll(long uid);


    @Select("select * from circle where state = 1 and id not in (select cid from circle_user where uid = #{uid} and " +
            "state = 1) " +
            "and (district_id = #{districtId} or district_id in (" +
            "select id from district where parent_id = #{districtId}))")
    @ResultMap("circleMap")
    List<Circle> selectAllByDistrict(Integer districtId, long uid);


    @Select("select * from circle where id = #{id} and state != 0")
    @ResultMap("circleMap")
    Circle selectById(Integer id);

    @Update("update circle set user_num = user_num + 1 where id = #{id} and state !=0")
    void joinMember(int id);

    @Update("update circle set user_num=user_num -1 where id = #{id} and state !=0")
    void quitMember(int id);

    @Update("update circle set article_num = article_num +1 where id = #{id} and state !=0")
    void addArticle(int id);

    @Update("update circle set article_num = article_num -1 where id = #{cid} and state !=0")
    int deleteArticle(Integer cid);

    @Select("select * from circle where state !=-1 and name like #{search} and id not in (select cid from circle_user" +
            " where " +
            "uid = " +
            "#{uid} and state!=0)")
    @ResultMap("circleMap")
    List<Circle> getSearchCircle(long uid, String search);

    @Update("update circle set state = 0 where id = #{id} and state = 1")
    int close(Integer id);
}
