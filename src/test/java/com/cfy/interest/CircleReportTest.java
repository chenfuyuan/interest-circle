package com.cfy.interest;

import com.cfy.interest.mapper.CircleReportMapper;
import com.cfy.interest.model.CircleReport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CircleReportTest {

    @Autowired
    private CircleReportMapper circleReportMapper;


    @Test
    public void testInsert() {


    }

    @Test
    public void select() {
        CircleReport circleReport = circleReportMapper.selectById(2);
        log.info(circleReport.toString());
    }
}
