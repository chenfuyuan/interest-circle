package com.cfy.interest.service;

import com.cfy.interest.model.User;

public interface IndexService {

    User signInByToken(String token);

}
