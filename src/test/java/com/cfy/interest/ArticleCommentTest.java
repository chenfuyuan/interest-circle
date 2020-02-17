package com.cfy.interest;

import com.cfy.interest.mapper.ArticleCommentMapper;
import com.cfy.interest.model.ArticleCommentReply;
import com.cfy.interest.model.ArticleCommentShow;
import com.cfy.interest.service.ArticleCommentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class ArticleCommentTest {

    @Autowired
    private ArticleCommentService articleCommentService;


    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Test
    public void testGetComments() {
        List<ArticleCommentShow> articleCommentShows = articleCommentMapper.selectByAid(10);
        log.info("articleCommentShows"+articleCommentShows);
        for (ArticleCommentShow articleCommentShow : articleCommentShows) {
            log.info("articleCommentShow = " + articleCommentShow);
            List<ArticleCommentReply> replys = articleCommentShow.getReplys();
            for (ArticleCommentReply reply : replys) {
                log.info("reply = " + reply);
            }
        }
    }

}
