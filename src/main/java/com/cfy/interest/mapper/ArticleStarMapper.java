package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleStar;
import org.apache.ibatis.annotations.Select;

public interface ArticleStarMapper extends BaseMapper<ArticleStar> {

    @Select("select * from article_star where uid = #{uid} and aid=#{aid} and state = 1")
    ArticleStar isStar(long uid, Integer aid);
}
