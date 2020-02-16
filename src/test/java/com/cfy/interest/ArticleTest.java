package com.cfy.interest;

import com.cfy.interest.mapper.ArticleMapper;
import com.cfy.interest.mapper.ArticleStickyMapper;
import com.cfy.interest.model.ArticleSticky;
import com.cfy.interest.service.vo.ArticleShow;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class ArticleTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleStickyMapper articleStickyMapper;


    @Test
    public void testGetStickys() {
        List<ArticleSticky> stickysByCid = articleStickyMapper.findStickysByCid(19);
        log.info(stickysByCid.toString());
    }

    @Test
    public void testGetArticleShow() {
        List<ArticleShow> articleShows = articleMapper.findByCid(19);
        log.info(articleShows.toString());

    }
}
