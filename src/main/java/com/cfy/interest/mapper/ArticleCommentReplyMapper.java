package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleCommentReply;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ArticleCommentReplyMapper extends BaseMapper<ArticleCommentReply> {

    @Select("select * from article_comment_reply where acid =#{acid} and state!=0 order by create_time asc")
    @Results(id = "replyMap",value = {
            @Result(property = "uid", column = "uid"),
            @Result(property = "user",column = "uid",one=@One(select = "com.cfy.interest.mapper.UserMapper" +
                    ".selectById")),
            @Result(property = "ruid", column = "ruid"),
            @Result(property = "replyUser",column = "ruid",one=@One(select = "com.cfy.interest.mapper.UserMapper.selectById")),
    })
    List<ArticleCommentReply> selectReplys(int acid);


    @Select("select * from article_comment_reply where id =#{id} and state!=0")
    @ResultMap("replyMap")
    ArticleCommentReply selectByAcId(Integer id);

    @Update("update article_comment_reply set state = 0 where acid = #{acid}")
    int deleteByAcid(Integer acid);

    @Update("update article_comment_reply set state = 0 where id = #{rid}")
    int deleteByRid(Integer rid);
}
