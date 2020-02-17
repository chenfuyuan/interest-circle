package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleReport;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface ArticleReportMapper extends BaseMapper<ArticleReport> {

    @Select("select * from article_report where uid = #{uid} and aid = #{aid} and state = 1")
    @Results(id="articleReportMap",value = {
            @Result(property = "uid", column = "uid"),
            @Result(property = "aid", column = "aid"),
            @Result(property = "article",column = "aid",one=@One(select = "com.cfy.interest.mapper.ArticleMapper.selectById")),
            @Result(property = "user",column = "uid",one=@One(select ="com.cfy.interest.mapper.UserMapper.selectById"))
    })
    ArticleReport selectByUidAndAid(Long uid, Integer aid);
}
