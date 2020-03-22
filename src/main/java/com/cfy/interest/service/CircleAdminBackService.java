package com.cfy.interest.service;

import com.cfy.interest.model.Circle;
import com.cfy.interest.model.CircleUser;
import com.cfy.interest.model.User;
import com.cfy.interest.vo.AddAdminVo;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CircleDayStatistics;
import com.cfy.interest.vo.CircleSettingSaveVo;

import java.util.List;

public interface CircleAdminBackService {

    /**
     * 圈子管理登录
     * @param uid
     * @param cid
     * @return
     */
    boolean login(Long uid,Integer cid);


    CircleDayStatistics circleStatisticsToday(Integer cid);

    CircleDayStatistics circleStatisticsYesterday(Integer cid);

    Circle selectCircleByCid(Integer cid);

    AjaxMessage saveSetting(CircleSettingSaveVo vo,Long uid);

    List<User> getAdminUserList(Integer cid);

    AjaxMessage deleteAdmin(Integer cid,Long uid, Long operatingUid) throws Exception;

    List<CircleUser> getMemberList(Integer cid);

    List<CircleUser> getMemberListByName(Integer cid, String search);

    AjaxMessage addAdminList(AddAdminVo addAdminVo, Long uid);
}
