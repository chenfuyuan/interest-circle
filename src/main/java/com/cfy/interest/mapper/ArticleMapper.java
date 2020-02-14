package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenfuyuan
 * @since 2020-02-14
 */
public interface ArticleMapper extends BaseMapper<Article> {


    @Select("select * from article where state!=0 order by #{sort} DESC")
    @Results(id = "articleMap", value = {
            @Result(property = "uid", column = "uid"),
            @Result(property = "cid", column = "cid"),
            @Result(property = "user", column = "uid", one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")),
            @Result(property = "circle", column = "cid", one = @One(select = "com.cfy.interest.mapper.CircleMapper.selectById"))
    })
    public List<Article> findByCid(int cid, String sort);

    @Select("select * from article where state!=0 and type = 2 order by #{sort} DESC ")
    @ResultMap("articleMap")
    public List<Article> findEssenceByCid(int cid, String sort);


}
