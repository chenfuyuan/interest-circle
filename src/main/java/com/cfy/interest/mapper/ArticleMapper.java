package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Article;
import com.cfy.interest.service.vo.ArticleShow;
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


    @Select("select * from article where state!=0 and cid=#{cid} ")
    @Results(id = "articleMap", value = {
            @Result(property = "uid", column = "uid"),
            @Result(property = "cid", column = "cid"),
            @Result(property = "user", column = "uid", one = @One(select = "com.cfy.interest.mapper.UserMapper.selectById")),
            @Result(property = "circle", column = "cid", one = @One(select = "com.cfy.interest.mapper.CircleMapper.selectById"))
    })
    public List<ArticleShow> findByCid(int cid);

    @Select("select * from article where state!=0 and type = 2 and cid=#{cid}  ")
    @ResultMap("articleMap")
    public List<ArticleShow> findEssenceByCid(int cid);

    @Select("SELECT count(0) FROM article WHERE state != 0 AND cid = #{cid} ")
    public int selectCountByCid(int cid);

    @Update("update article set like_num = like_num+1 where id =#{aid}")
    int like(Integer aid);

    @Update("update article set like_num = like_num-1 where id =#{aid}")
    int cancelLike(Integer aid);
}
