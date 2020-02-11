package com.cfy.interest;

import com.cfy.interest.mapper.CircleUserMapper;
import com.cfy.interest.model.User;
import com.cfy.interest.model.UserOwnCircle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class CircleUserTest {
    @Autowired
    private CircleUserMapper circleUserMapper;

    @Test
    public void testSelectByUid() {
        List<UserOwnCircle> ownCircles = circleUserMapper.selectByUid(7);
        for (UserOwnCircle userOwnCircles : ownCircles) {
            log.info("userOwnCircle = " + userOwnCircles);
        }
    }

    @Test
    public void testSelectCircleUserByCid() {
        List<User> userList = circleUserMapper.selectCircleUserByCid(12);
        log.info(userList.toString());
    }
}
