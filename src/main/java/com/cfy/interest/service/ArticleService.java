package com.cfy.interest.service;

import com.cfy.interest.model.Article;
import com.cfy.interest.model.ArticleSticky;
import com.cfy.interest.service.vo.EditorArticleVo;
import com.cfy.interest.service.vo.GetArticleVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<String> uploadImages(MultipartFile[] files);

    List<ArticleSticky> getStickys(int cid);

    void publish(long uid, EditorArticleVo editorArticleVo);

    List<Article> getArticles(GetArticleVo getArticleVo);
}
