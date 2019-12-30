package com.cfy.interest.service;

import com.cfy.interest.service.vo.SignInMessage;
import com.cfy.interest.service.vo.SignInVo;

public interface SignInService {

    SignInMessage checksignIn(SignInVo signInVo);

    boolean rememberPassword(SignInMessage signInMessage);

}
