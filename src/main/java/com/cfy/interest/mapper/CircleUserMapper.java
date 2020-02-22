package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.CircleUser;
import com.cfy.interest.model.User;
import com.cfy.interest.model.UserOwnCircle;
import com.cfy.interest.vo.CircleDayStatistics;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CircleUserMapper extends BaseMapper<CircleUser> {

    /**
     * 通过uid查询该用户所在的圈子
     * @param uid
     * @return
     */
    @Select("select * from circle_user where uid = #{uid} and state!=0 order by update_time desc")
    @Results(id="UserOwnMap",value = {
            @Result(property = "cid",column = "cid"),
            @Result(property = "circle",column = "cid",one = @One(select = "com.cfy.interest.mapper.CircleMapper.selectById"))
    })
    public List<UserOwnCircle> selectByUid(long uid);

    @Select("select u.* from User u join circle_user cu on u.id = cu.uid where cu.cid = #{cid} and cu.state!=0 order" +
            " " +
            "by " +
            "type asc")
    public List<User> selectCircleUserByCid(int cid);


    @Update("update circle_user set state = 0 where cid=#{cid} and uid=#{uid}")
    void quit(long uid, Integer cid);


    @Select("select count(0) from circle_user where state != 0 and uid = #{uid} and cid = #{cid} and type = 1")
    Integer login(Long uid, Integer cid);



}
