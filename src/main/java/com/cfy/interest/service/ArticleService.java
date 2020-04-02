package com.cfy.interest.service;

import com.cfy.interest.model.Article;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ArticleShow;
import com.cfy.interest.vo.EditorArticleVo;
import com.cfy.interest.vo.GetArticleVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<String> uploadImages(MultipartFile[] files);

    List<Article> getStickys(int cid);

    void publish(long uid, EditorArticleVo editorArticleVo);

    List<ArticleShow> getArticles(GetArticleVo getArticleVo,long uid);

    int selectCountByCId(int cid);

    AjaxMessage like(long uid, Integer aid,Integer cid);

    AjaxMessage cancelLike(long uid, Integer aid,Integer cid);

    boolean isLike(long uid, Integer aid);

    Article sticky(long uid, Integer aid,Integer cid);

    AjaxMessage essence(long uid, Integer aid,Integer cid);

    AjaxMessage cancelSticky(long uid, Integer aid,Integer cid);

    AjaxMessage cancelEssence(long uid, Integer aid,Integer cid);

    AjaxMessage star(long uid, Integer aid,Integer cid);

    AjaxMessage cancelStar(long uid, Integer aid,Integer cid);

    AjaxMessage delete(Long uid, Integer aid, Integer cid) throws Exception;


    ArticleShow getArticle(Integer aid, int cid,Long uid);

    List<ArticleShow> getStickList(Long uid,Integer cid);
}
