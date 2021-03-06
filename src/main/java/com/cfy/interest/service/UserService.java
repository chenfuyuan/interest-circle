package com.cfy.interest.service;

import com.cfy.interest.model.Article;
import com.cfy.interest.model.User;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ChangePasswordVo;
import com.cfy.interest.vo.SendSmsMessage;
import com.cfy.interest.vo.UserInfoVo;

import java.util.List;

public interface UserService {

    public AjaxMessage update(User user, UserInfoVo userInfoVo);

    public boolean nameExist(String name,long uid);

   SendSmsMessage sendSms(String phone);

    boolean checkAuthCode(String phone, String authCode);

    void changePassword(User user, ChangePasswordVo changePasswordVo);

    List<Article> getMyArticles(Long uid);

    List<Article> getMyStar(Long uid);

    List<Article> getMyLike(Long uid);
}
