package com.cfy.interest.service;

import com.cfy.interest.model.Article;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.ArticleShow;
import com.cfy.interest.service.vo.EditorArticleVo;
import com.cfy.interest.service.vo.GetArticleVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<String> uploadImages(MultipartFile[] files);

    List<Article> getStickys(int cid);

    void publish(long uid, EditorArticleVo editorArticleVo);

    List<ArticleShow> getArticles(GetArticleVo getArticleVo,long uid);

    int selectCountByCId(int cid);

    AjaxMessage like(long uid, Integer aid);

    AjaxMessage cancelLike(long uid, Integer aid);

    boolean isLike(long uid, Integer aid);

    Article sticky(long uid, Integer aid);

    AjaxMessage essence(long uid, Integer aid);

    AjaxMessage cancelSticky(long uid, Integer aid);

    AjaxMessage cancelEssence(long uid, Integer aid);

    AjaxMessage star(long uid, Integer aid);

    AjaxMessage cancelStar(long uid, Integer aid);

    AjaxMessage delete(Long uid, Integer aid, Integer cid) throws Exception;


}
