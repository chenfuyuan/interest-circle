package com.cfy.interest;

import com.cfy.interest.mapper.ArticleMapper;
import com.cfy.interest.vo.ArticleShow;
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




    @Test
    public void testGetArticleShow() {

        List<ArticleShow> articleShows = articleMapper.findByCid(19);
        log.info(articleShows.toString());
    }
}
