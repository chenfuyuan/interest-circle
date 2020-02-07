package com.cfy.interest;

import com.cfy.interest.service.CircleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CreateCircleTest {

    @Autowired
    private CircleService createCircleService;

    @Test
    public void existCircle() {
        createCircleService.existCircle("学霸圈子");
    }

}
