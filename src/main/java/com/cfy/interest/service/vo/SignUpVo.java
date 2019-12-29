package com.cfy.interest.service.vo;

import lombok.Data;

@Data
public class SignUpVo {
    private String name;
    private String password;
    private String authCode;
    private String phone;
}
