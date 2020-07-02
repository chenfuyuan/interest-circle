package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.ArticleLike;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {

    @Update("update article_like set state = 1 where aid = #{aid} and uid = #{uid} and state = 0")
    public int like(Integer aid, Long uid);

    @Update("update article_like set state = 0 where aid = #{aid} and uid = #{uid} and state = 1")
    int cancelLike(Integer aid, Long uid);

    @Select("select count(0) from article_like where uid = #{uid} and aid=#{aid} and state = 1")
    int isLike(long uid, Integer aid);

    @Update("update article_like set state = 0 where aid = #{aid} and state = 1")
    int cancelLikeByAid(Integer aid);

}
