package com.cfy.interest;

import com.cfy.interest.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OperationTest {

    @Autowired
    private ArticleService articleService;


    @Test
    public void testLike() {
        for (long uid = 13; uid < 33; uid+=2) {
            articleService.like(uid,20,27);
        }
    }

    @Test
    public void testStar() {
    }
}
