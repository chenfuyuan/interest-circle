package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleOperationMessage;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenfuyuan
 * @since 2020-02-14
 */
public interface ArticleOperationMessageMapper extends BaseMapper<ArticleOperationMessage> {


    @Select("select count(0) from article_operation_message where cid=#{cid} and to_days(create_time) = to_days(now())")
    Integer statisticsInteractToday(Integer cid);

    @Select("select count(0) from article_operation_message where (type = 4 or type =6 or type = 1 or type = 14 or " +
            "type = 15)and " +
            "cid=#{cid} and" +
            " " +
            "to_days(now()) - " +
            "to_days" +
            "(create_time) = 1")
    Integer statisticsInteractYesterday(Integer cid);


    @Select("select count(0) from article_operation_message where type = 1 and cid=#{cid} and to_days(create_time) = " +
            "to_days(now())")
    Integer statisticsNewArticleToday(Integer cid);

    @Select("select count(0) from article_operation_message where type = 1 and cid = #{cid} and to_days(now()) - " +
            "to_days(create_time) = 1")
    Integer statisticsNewArticleYesterday(Integer cid);

    @Select("select count(0) from article_operation_message where (type = 14 or type = 15) and cid=#{cid} and to_days" +
            "(create_time) = " +
            "to_days(now())")
    Integer statisticsNewCommentToday(Integer cid);

    @Select("select count(0) from article_operation_message where (type = 14 or type = 15) and cid=#{cid} and to_days" +
            "(now()) - " +
            "to_days(create_time) =1")
    Integer statisticsNewCommentYesterday(Integer cid);


}
