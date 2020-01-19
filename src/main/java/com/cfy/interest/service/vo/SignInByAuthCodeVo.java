package com.cfy.interest.service.vo;

import lombok.Data;

@Data
public class SignInByAuthCodeVo {
    private String phone;
    private String authCode;
    private boolean rememberPassword;
}
