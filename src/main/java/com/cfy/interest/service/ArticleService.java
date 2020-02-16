package com.cfy.interest.service;

import com.cfy.interest.model.ArticleSticky;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.ArticleShow;
import com.cfy.interest.service.vo.EditorArticleVo;
import com.cfy.interest.service.vo.GetArticleVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<String> uploadImages(MultipartFile[] files);

    List<ArticleSticky> getStickys(int cid);

    void publish(long uid, EditorArticleVo editorArticleVo);

    List<ArticleShow> getArticles(GetArticleVo getArticleVo,long uid);

    int selectCountByCId(int cid);

    AjaxMessage like(long uid, Integer aid);

    AjaxMessage cancelLike(long uid, Integer aid);

    boolean isLike(long uid, Integer aid);
}
