package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.CircleOperationMessageMapper;
import com.cfy.interest.mapper.CircleUserMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.model.CircleOperationMessage;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.CircleAdminBackService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CircleDayStatistics;
import com.cfy.interest.vo.CircleSettingSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AliyunOSSProvider aliyunOSSProvider;

    /**
     * 圈子管理登录
     *
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public boolean login(Long uid, Integer cid) {
        Circle circleByRedis = (Circle) redisTemplate.opsForValue().get("c" + cid);
        if (circleByRedis != null) {
            log.info("查询的是缓存");
            return circleByRedis.getOwnerId() == uid;
        }
        //缓存中未有数据，查询数据库
        Circle circle = circleMapper.selectById(cid);
        log.info("cricle.owner = " + circle.getOwner());
        log.info("cricle.ownerId = " + circle.getOwnerId());
        if (circle == null) {
            log.info("所查询的圈子不存在");
            return false;
        }
        //将数据放入缓存中
        redisTemplate.opsForValue().set("c" + cid, circle);
        if (circle.getOwnerId() == uid) {
            return true;
        } else {
            log.info("登录用户不是该圈子成员");
            return false;
        }
    }

    /**
     * 统计圈子数据
     *
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

    @Transactional
    @Override
    public AjaxMessage saveSetting(CircleSettingSaveVo vo,Long uid) {
        aliyunOSSProvider.initOssClient();
        //1.保存封面
        String bgdPath = null;
        if (vo.getBackground() != null) {
            bgdPath = aliyunOSSProvider.uploadImg2Oss(vo.getBackground());
        }
        //2.保存头像
        String avatarPath = null;
        if (vo.getAvatar() != null) {
            avatarPath = aliyunOSSProvider.uploadImg2Oss(vo.getAvatar());
        }

        Circle circle = new Circle();
        circle.setId(vo.getCid());
        circle.setName(vo.getName());
        circle.setIntroduce(vo.getIntroduce());
        circle.setAvatarPath(avatarPath);
        circle.setBgdPath(bgdPath);

        //保存更新
        circleMapper.updateById(circle);

        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(uid,vo.getCid());
        circleOperationMessage.setType(5);
        circleOperationMessage.setMessage("修改圈子信息");
        circleOperationMessageMapper.insert(circleOperationMessage);

        return new AjaxMessage(true, "修改成功");

    }
}
