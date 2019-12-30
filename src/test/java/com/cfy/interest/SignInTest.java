package com.cfy.interest;

import com.cfy.interest.service.SignInService;
import com.cfy.interest.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SignInTest {
    @Autowired
    SignInService signInService;
    @Test
    public void signIn() {


    }

    @Test
    public void md5Test() {
        String s = MD5Utils.MD5Encode("123456");
        System.out.println(s);
    }
}
