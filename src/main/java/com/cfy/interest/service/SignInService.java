package com.cfy.interest.service;

import com.cfy.interest.vo.SignInMessage;
import com.cfy.interest.vo.SignInVo;

public interface SignInService {

    SignInMessage checksignIn(SignInVo signInVo);

}
