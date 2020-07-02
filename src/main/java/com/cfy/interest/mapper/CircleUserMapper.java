package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.CircleUser;
import com.cfy.interest.model.User;
import com.cfy.interest.model.UserOwnCircle;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CircleUserMapper extends BaseMapper<CircleUser> {

    /**
     * 通过uid查询该用户所在的圈子
     * @param uid
     * @return
     */
    @Select("select * from circle_user where uid = #{uid} and state!=0 order by create_time asc")
    @Results(id="UserOwnMap",value = {
            @Result(property = "cid",column = "cid"),
            @Result(property = "circle",column = "cid",one = @One(select = "com.cfy.interest.mapper.CircleMapper" +
                    ".selectById"))
    })
    public List<UserOwnCircle> selectByUid(long uid);

    @Select("select u.* from User u join circle_user cu on u.id = cu.uid where u.state!=0 and  cu.cid = #{cid} and cu" +
            ".state!=0 order" +
            " " +
            "by " +
            "type asc limit 4")
    public List<User> selectCircleUserByCid(int cid);


    @Update("update circle_user set state = 0 where cid=#{cid} and uid=#{uid}")
    void quit(long uid, Integer cid);


    @Select("select count(0) from circle_user where state != 0 and uid = #{uid} and cid = #{cid} and type = 1")
    Integer login(Long uid, Integer cid);

    @Select("select * from user where id in (select uid from circle_user where cid = #{cid} and type =2 and state = 1)")
    List<User> getAdminUserList(Integer cid);

    @Update("update circle_user set type = 3 where state = 1 and cid = #{cid} and uid = #{uid} and type = 2")
    int deleteAdminByCid(Integer cid, Long uid);

    @Select("select * from circle_user where cid = #{cid} and state =1 and type!= 1")
    @Results(id="CircleUser",value = {
            @Result(property = "cid",column = "cid"),
            @Result(property = "circle",column = "cid",one = @One(select = "com.cfy.interest.mapper.CircleMapper" +
                    ".selectById")),
            @Result(property = "uid", column = "uid"),
            @Result(property = "user",column = "uid",one = @One(select = "com.cfy.interest.mapper.UserMapper" +
                    ".selectById"))
    })
    List<CircleUser> getMemberList(Integer cid);


    @Select("select * from circle_user where cid = #{cid} and state =1 and type!= 1 and uid in (select id from user " +
            "where name like #{search})")
    @ResultMap("CircleUser")
    List<CircleUser> getMemberListByName(Integer cid, String search);

    @UpdateProvider(type = CircleUserSqlModel.class,method = "deleteAdminList")
    Integer deleteAdminByList(List<Integer> deleteAdminArray, Integer cid);

    @UpdateProvider(type = CircleUserSqlModel.class, method = "addAdminList")
    Integer addAdminByList(List<Integer> addAdminArray, Integer cid);


    @Update("update circle_user set state = 1 where uid =#{uid} and cid = #{cid}")
    int UpdateJoinCircle(long uid, Integer cid);

    @Update("update circle_user set state = 0 where state = 1 and cid = #{cid}")
    int close(Integer cid);
}
