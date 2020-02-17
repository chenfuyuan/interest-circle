package com.cfy.interest.vo;

import lombok.Data;

@Data
public class SignInByAuthCodeVo {
    private String phone;
    private String authCode;
    private boolean rememberPassword;
}
