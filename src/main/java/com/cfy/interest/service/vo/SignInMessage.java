package com.cfy.interest.service.vo;

import com.cfy.interest.model.User;
import lombok.Data;

@Data
public class SignInMessage {
    private String message;
    private boolean success;
    private boolean isRememberPassword;
    private User user;
}
