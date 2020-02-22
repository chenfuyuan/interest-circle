package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.CircleOperationMessageMapper;
import com.cfy.interest.mapper.CircleUserMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.service.CircleAdminBackService;
import com.cfy.interest.vo.CircleDayStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CircleAdminBackServiceImpl implements CircleAdminBackService {


    @Autowired
    private CircleUserMapper circleUserMapper;


    @Autowired
    private CircleOperationMessageMapper circleOperationMessageMapper;

    @Autowired
    private ArticleOperationMessageMapper articleOperationMessageMapper;

    @Autowired
    private CircleMapper circleMapper;
    /**
     * 圈子管理登录
     *
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public boolean login(Long uid, Integer cid) {
        Integer row = circleUserMapper.login(uid, cid);
        if (row == 1) {
            return true;
        }
        return false;
    }

    /**
     * 统计圈子数据
     * @param cid
     * @return
     */
    @Override
    public CircleDayStatistics circleStatisticsToday(Integer cid) {
        CircleDayStatistics circleDayStatistics = new CircleDayStatistics();

        //加入圈子数据
        Integer joinNum = circleOperationMessageMapper.statisticsJoinToday(cid);
        circleDayStatistics.setJoin(joinNum);


        //互动数据
        Integer interact = articleOperationMessageMapper.statisticsInteractToday(cid);
        circleDayStatistics.setInteract(interact);

        //新增帖子
        Integer newArticle = articleOperationMessageMapper.statisticsNewArticleToday(cid);
        circleDayStatistics.setNewArticle(newArticle);

        //新增回复
        Integer newComment = articleOperationMessageMapper.statisticsNewCommentToday(cid);
        circleDayStatistics.setNewComment(newComment);

        return circleDayStatistics;
    }

    @Override
    public CircleDayStatistics circleStatisticsYesterday(Integer cid) {
        CircleDayStatistics circleDayStatistics = new CircleDayStatistics();

        //加入圈子数据
        Integer joinNum = circleOperationMessageMapper.statisticsJoinYesterday(cid);
        circleDayStatistics.setJoin(joinNum);


        //互动数据
        Integer interact = articleOperationMessageMapper.statisticsInteractYesterday(cid);
        circleDayStatistics.setInteract(interact);

        //新增帖子
        Integer newArticle = articleOperationMessageMapper.statisticsNewArticleYesterday(cid);
        circleDayStatistics.setNewArticle(newArticle);

        //新增回复
        Integer newComment = articleOperationMessageMapper.statisticsNewCommentYesterday(cid);
        circleDayStatistics.setNewComment(newComment);

        return circleDayStatistics;
    }

    @Override
    public Circle selectCircleByCid(Integer cid) {
        return circleMapper.selectById(cid);
    }
}
