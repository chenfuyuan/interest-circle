package com.cfy.interest;

import com.cfy.interest.mapper.UserMapper;
import com.cfy.interest.service.SignInByAuthCodeService;
import com.cfy.interest.vo.SignInByAuthCodeVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SignInByAuthCodeTest {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SignInByAuthCodeService service;

    /**
     * 测试电话号码是否存在
     */
    @Test
    public void phoneIsExist() {
        boolean phoneIsExist = service.phoneIsExist("18059851006");
        System.out.println("phoneIsExist ? " + phoneIsExist);
    }

    @Test
    public void sendSignInMessage() {
        service.SendSignInMessage("18059851006");
    }

    @Test
    public void testSignInByAuthCode() {
        SignInByAuthCodeVo signInByAuthCodeVo = new SignInByAuthCodeVo();
        signInByAuthCodeVo.setPhone("18059851005");
        signInByAuthCodeVo.setAuthCode("56397");
        signInByAuthCodeVo.setRememberPassword(false);
        System.out.println(service.signInByAuthCode(signInByAuthCodeVo));
    }

}
