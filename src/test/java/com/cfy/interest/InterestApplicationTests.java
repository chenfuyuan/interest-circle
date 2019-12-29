package com.cfy.interest;

import com.cfy.interest.service.SignUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterestApplicationTests {


    @Autowired
    private SignUpService signUpService;

    @Test
    public void sendSms() {
        signUpService.sendSms("15659280183");
    }

    @Test
    public void testUserMapper() {
        boolean isExist = signUpService.phoneIsExist("18059851006");
        System.out.println(isExist);
    }

    @Test
    public void testRedis() {
        signUpService.sendSms("18059851006");
    }

    @Test
    public void testNameExist() {
        System.out.println(signUpService.nameIsExist("chenfuyuan"));

    }


}
