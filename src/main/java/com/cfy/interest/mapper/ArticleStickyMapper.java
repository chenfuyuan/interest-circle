package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleSticky;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleStickyMapper extends BaseMapper<ArticleSticky> {

    @Select("select * from article_sticky where cid = #{cid} and state = 1")
    @Results(id="stickyMap",value ={
            @Result(property = "aid", column = "aid"),
            @Result(property = "article",column = "aid",one=@One(select = "com.cfy.interest.mapper.ArticleMapper.selectById"))
    })
    public List<ArticleSticky> findStickysByCid(int cid);
}
