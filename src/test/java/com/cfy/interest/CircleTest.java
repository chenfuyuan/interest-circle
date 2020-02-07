package com.cfy.interest;


import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.service.CircleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CircleTest {

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleService circleService;
    @Test
    public void testInsert() {
        Circle circle = new Circle();
        circle.setName("aaaa");
        circleMapper.insert(circle);

        log.info(circle.toString());
    }


}
