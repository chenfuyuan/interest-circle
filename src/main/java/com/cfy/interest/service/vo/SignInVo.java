package com.cfy.interest.service.vo;

import lombok.Data;

@Data
public class SignInVo {
    private String phone;
    private String password;
    private boolean rememberPassword;
}

