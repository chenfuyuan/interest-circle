package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleStar;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ArticleStarMapper extends BaseMapper<ArticleStar> {

    @Select("select count(0) from article_star where uid = #{uid} and aid=#{aid} and state = 1")
    int isStar(long uid, Integer aid);

    @Update("update article_star set state = 1 where state = 0 and aid =#{aid} and #{uid}")
    int star(Integer aid,Long uid);

    @Update("update article_star set state = 0 where state = 1 and aid =#{aid} and #{uid}")
    int cancelStar(Integer aid,Long uid);

    @Update("update article_star set state = 0 where state = 1 and aid =#{aid}")
    int cancelStarByAid(Integer aid);

}
