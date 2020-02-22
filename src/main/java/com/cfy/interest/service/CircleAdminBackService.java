package com.cfy.interest.service;

import com.cfy.interest.model.Circle;
import com.cfy.interest.vo.CircleDayStatistics;

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
}
