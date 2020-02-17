package com.cfy.interest;

import com.cfy.interest.service.SignUpService;
import com.cfy.interest.vo.SignUpVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class SignUpTest {

    @Autowired
    private SignUpService signUpService;

    @Test
    public void testSendSms() {
        signUpService.sendSms("18059851006");
    }
    @Test
    public void testCheckAuthCode() {
        signUpService.checkAuthCode("18059851006", "437365");
    }

    @Test
    public void testDateToLong() {
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime());
    }

    @Test
    public void SaveUser() {
        SignUpVo signUpVo = new SignUpVo();
        signUpVo.setName("chenfuyuan");
        signUpVo.setPassword("123456");
        signUpVo.setPhone("18059851006");
        signUpService.saveUser(signUpVo);
    }
}
