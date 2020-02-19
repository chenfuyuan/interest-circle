package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleComment;
import com.cfy.interest.model.ArticleCommentShow;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {


    @Select("select * from article_comment where aid = #{aid} and state!=0")
    @Results(id="commentMap",value={
            @Result(property = "uid", column = "uid"),
            @Result(property = "user",column = "uid",one=@One(select = "com.cfy.interest.mapper.UserMapper" +
                    ".selectById")),
            @Result(property = "id", column = "id"),
            @Result(property ="replys",column = "id",javaType = List.class,many = @Many(select = "com.cfy.interest.mapper.ArticleCommentReplyMapper.selectReplys"))
    })
    List<ArticleCommentShow> selectByAid(Integer aid);

    @Update("update article_comment set reply_num = reply_num + 1 where id =#{acid} and state != 0")
    int reply(Integer acid);

    @Select("select * from article_comment where id = #{id} and state != 0")
    @ResultMap("commentMap")
    public ArticleComment selectById(Integer id);

    @Select("select count(0) from article_comment where aid = #{aid} and state != 0")
    int selectCountByAid(Integer aid);

    @Update("update article_comment set state = 0 where id = #{acid}")
    int deleteByAcId(Integer acid);

    @Update("update article_comment set reply_num = reply_num -1 where id = #{acid} and state !=0")
    int deleteReply(Integer acid);
}
