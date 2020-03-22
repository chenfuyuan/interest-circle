package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.CircleOperationMessageMapper;
import com.cfy.interest.mapper.CircleUserMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.model.CircleOperationMessage;
import com.cfy.interest.model.CircleUser;
import com.cfy.interest.model.User;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.CircleAdminBackService;
import com.cfy.interest.vo.AddAdminVo;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CircleDayStatistics;
import com.cfy.interest.vo.CircleSettingSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public AjaxMessage saveSetting(CircleSettingSaveVo vo, Long uid) {
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

        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(uid, vo.getCid());
        circleOperationMessage.setType(5);
        circleOperationMessage.setMessage("修改圈子信息");
        circleOperationMessageMapper.insert(circleOperationMessage);

        return new AjaxMessage(true, "修改成功");

    }

    @Override
    public List<User> getAdminUserList(Integer cid) {
        return circleUserMapper.getAdminUserList(cid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxMessage deleteAdmin(Integer cid, Long uid, Long operatingUid) throws Exception {
        //删除管理员
        int changeRow = circleUserMapper.deleteAdminByCid(cid, uid);
        if (changeRow == 0) {
            throw new Exception("该圈子未有该管理员");
        }
        if (changeRow > 1) {
            throw new Exception("删除cuow");
        }

        //添加用户操作记录
        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(uid, cid);
        circleOperationMessage.setType(6);
        circleOperationMessage.setMessage("删除管理员");

        circleOperationMessageMapper.insert(circleOperationMessage);

        return new AjaxMessage(true, "删除成功");


    }

    @Override
    public List<CircleUser> getMemberList(Integer cid) {
        List<CircleUser> userList = circleUserMapper.getMemberList(cid);
        return userList;
    }

    @Override
    public List<CircleUser> getMemberListByName(Integer cid, String search) {
        search = "%" + search + "%";
        List<CircleUser> userList = circleUserMapper.getMemberListByName(cid, search);
        return userList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxMessage addAdminList(AddAdminVo addAdminVo, Long uid) {
        Integer cid = addAdminVo.getCid();
        List<Integer> deleteAdminList = addAdminVo.getDeleteAdminArray();
        List<Integer> addAdminList = addAdminVo.getAddAdminArray();

        //删除管理员
        if (deleteAdminList.size() != 0) {
            int changeRow = circleUserMapper.deleteAdminByList(deleteAdminList, addAdminVo.getCid());
            if (changeRow != 0) {
                //添加操作日志
                CircleOperationMessage deleteCircleOperationMessage = new CircleOperationMessage().build(uid, cid);
                deleteCircleOperationMessage.setType(6);
                deleteCircleOperationMessage.setMessage("删除管理员");
                circleOperationMessageMapper.insert(deleteCircleOperationMessage);
            }
        }
        if (addAdminList.size() != 0) {
            //添加管理员
            int changeRow = circleUserMapper.addAdminByList(addAdminList, addAdminVo.getCid());
            if (changeRow != 0) {
                //添加操作日志
                CircleOperationMessage addCircleOperationMessage = new CircleOperationMessage().build(uid, cid);
                addCircleOperationMessage.setType(7);
                addCircleOperationMessage.setMessage("添加管理员");
                circleOperationMessageMapper.insert(addCircleOperationMessage);
            }
        }
        return new AjaxMessage(true, "操作成功");
    }
}
