package com.cfy.interest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfy.interest.model.Article;
import com.cfy.interest.vo.ArticleShow;
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

    @Select("select * from article where state!=0 and cid=#{cid} and (title like #{search} or content like #{search}) ")
    @ResultMap("articleMap")
    public List<ArticleShow> findSearchByCid(int cid,String search);

    @Select("select * from article where state!=0 and type = 2 and cid=#{cid}  ")
    @ResultMap("articleMap")
    public List<ArticleShow> findEssenceByCid(int cid);

    @Select("select * from article where state!=0 and type = 2 and cid=#{cid} and (title like #{search or content " +
            "like #{search}}) ")
    @ResultMap("articleMap")
    List<ArticleShow> findEssenceSearchByCid(int cid, String search);

    @Select("SELECT count(0) FROM article WHERE state != 0 AND cid = #{cid} ")
    public int selectCountByCid(int cid);

    @Update("update article set like_num = like_num+1 where id =#{aid}")
    int like(Integer aid);

    @Update("update article set like_num = like_num-1 where id =#{aid}")
    int cancelLike(Integer aid);

    @Update("update article set sticky = 1 where id =#{aid} and state!=0")
    int stickyArticle(Integer aid);

    @Select("select * from article where sticky = 1 and state != 0 and cid=#{cid}")
    @ResultMap("articleMap")
    List<Article> findStickysByCid(int cid);

    @Update("update article set type = 2 where state != 0 and id=#{aid}")
    int essenceByAid(Integer aid);

    @Update("update article set sticky = 0 where state !=0 and id=#{aid}")
    int cancelSticky(Integer aid);

    @Update("update article set type = 1 where state !=0 and id=#{aid}")
    int cancelEssence(Integer aid);

    @Update("update article set star_num = star_num +1 where state !=0 and id=#{aid}")
    int star(Integer aid);

    @Update("update article set star_num = star_num -1 where state != 0 and id=#{aid}")
    int cancelStar(Integer aid);

    @Update("update article set state = 0 where id = #{aid} and cid =#{cid}")
    int deleteById(Integer aid,Integer cid);

    @Select("select * from article where state != 0 and id =#{aid}")
    @ResultMap("articleMap")
    ArticleShow selectShowById(Integer aid);

    @Update("update article set comment_num = comment_num+1 where id = #{aid} and state != 0")
    int comment(int aid);

    @Update("update article set comment_num = comment_num+1 where id = #{aid} and state != 0")
    int reply(Integer aid);

    @Update("update article set comment_num = comment_num - #{num} where id = #{aid} and state!=0")
    int deleteComment(Integer aid, int num);

    @Update("update article set comment_num = comment_num - 1 where id = #{aid} and state!=0")
    int deleteReply(Integer aid);

    @Select("select * from article where state = 1 and cid = #{cid} order by like_num desc limit 10")
    List<Article> selectHotArticleByCid(Integer cid);
}
