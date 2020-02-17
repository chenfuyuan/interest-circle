package com.cfy.interest.vo;

import lombok.Data;

@Data
public class SignUpMessage {
    private String phone;
    private boolean success;
    private String message;

}
