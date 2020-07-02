package com.cfy.interest;


import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.CircleUserMapper;
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
    private CircleUserMapper circleUserMapper;
    @Autowired
    private CircleService circleService;
    @Test
    public void testInsert() {
        Circle circle = new Circle();
        circle.setName("aaaa");
        circleMapper.insert(circle);

        log.info(circle.toString());
    }

    @Test
    public void testResults() {
        log.info(circleMapper.selectById(12).toString());
    }


    @Test
    public void testJoinMember() {
        circleMapper.joinMember(12);
    }

    @Test
    public void testJoinCircle() {
        for (long uid = 13; uid < 33; uid++) {
            circleService.joinCircle(uid,27);
        }

    }


    @Test
    public void createCircleTest() {
//        for (int i = 0; i < 1000; i++) {
//            Circle circle = new Circle();
//            circle.setName("测试" + i);
//            int district = (int)(Math.random() * 532 + 1);
//            circle.setDistrictId(district);
//            circle.setBgdPath("/static/image/bgd.png");
//            circle.setAvatarPath("http://chenfuyuan.oss-cn-shenzhen.aliyuncs.com/interest-circle/2020-03-25/5fc5e5c85f204e5ebf438f237f328c7f.jpg");
//            circle.setUserNum(1);
//            circle.setOwnerId(34L);
//            circleMapper.insert(circle);
//
//            //建立圈子之间联系
//            CircleUser circleUser = CircleUser.build(34L, circle.getId());
//            circleUser.setType(1);
//            circleUserMapper.insert(circleUser);
//        }
    }
    @Test
    public void joinCircle(){

        //圈子数2034 - 3030
        int cid = 2034;


            long uid = 34;
            for(long j= uid ; j<=53 ;j++){

                    circleService.joinCircle(j,3034);

           }



    }
}
