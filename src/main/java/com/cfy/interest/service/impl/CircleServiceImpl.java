package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.*;
import com.cfy.interest.model.*;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.CircleService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CreateCircleFormVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CircleServiceImpl implements CircleService {


    @Autowired
    private CircleMapper circleMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AliyunOSSProvider aliyunOSSProvider;

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private CircleOperationMessageMapper circleOperationMessageMapper;

    @Autowired
    private CircleUserMapper circleUserMapper;

    @Autowired
    private ArticleMapper articleMapper;
    /**
     * 获取省份列表
     *
     * @return
     */
    @Override
    public List<Province> getProvinces() {
        //从数据库中获取省份列表
        List<Province> provinces;
        //从redis中查询缓存
        //查询出整个list
        provinces = redisTemplate.opsForList().range("provinces", 0, -1);

        //如果为null或为空,查询数据库
        if (provinces == null || provinces.size() == 0) {
            provinces = districtMapper.getProvinces();
            //添加入缓存，尾插法添加数据
            redisTemplate.opsForList().rightPushAll("provinces", provinces);
            System.out.println("添加入缓存");
        }

        return provinces;
    }

    @Override
    public List<City> getCityByProvincesId(int id) {
        List<City> cities;
        Province province = (Province) redisTemplate.opsForList().index("provinces", id - 1);
        if (province != null) {
            System.out.println("从redis中获取citys");
            cities = province.getCitys();
        } else {
            cities = districtMapper.findCityByProvince(id);
        }
        return cities;
    }


    @Override
    public Circle existCircle(String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        Circle circle = circleMapper.findByName(name);
        log.info("circle = " + circle);
        return circle;
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Override
    public AjaxMessage createCircle(CreateCircleFormVo createCircleFormVo, long uid) {
        AjaxMessage ajaxMessage = new AjaxMessage();
        String name = createCircleFormVo.getCircleName();
        int province = createCircleFormVo.getProvince();
        int city = createCircleFormVo.getCity();
        if (existCircle(name) != null) {
            ajaxMessage.setSuccess(false);
            ajaxMessage.setMessage("圈子已存在");
            return ajaxMessage;
        }

        //上传图片到oss服务器上
        String filePath = uploadAvatar(createCircleFormVo.getAvatar());
        if (filePath.equals("") || filePath == null) {
            ajaxMessage.setMessage("图片上传失败");
            ajaxMessage.setSuccess(false);
        }

        //填充数据
        Circle circle = new Circle();
        //填充所处地区
        if (province == 0) {
            circle.setDistrictId(0);
        } else if (city == -1) {
            circle.setDistrictId(province);
        } else {
            circle.setDistrictId(city);
        }
        circle.setName(name);
        circle.setUserNum(1);
        circle.setArticleNum(0);
        circle.setState(1);
        circle.setOwnerId(uid);
        circle.setAvatarPath(filePath);
        long nowTime = new Date().getTime();

        //操作数据库
        //将创建的圈子信息插入数据库
        circleMapper.insert(circle);
        int cid = circle.getId();
        redisTemplate.opsForValue().set("circle:" + cid, circle);
        circle.setId(cid);

        CircleUser circleUser = CircleUser.build(uid, cid);
        circleUser.setType(1);
        circleUserMapper.insert(circleUser);

        //圈子操作记录
        CircleOperationMessage circleOperationMessage = new CircleOperationMessage();
        circleOperationMessage.setCId(cid);
        circleOperationMessage.setUId(uid);
        circleOperationMessage.setMessage("创建圈子");
        circleOperationMessage.setType(1);
        circleOperationMessageMapper.insert(circleOperationMessage);
        ajaxMessage.setSuccess(true);
        ajaxMessage.setMessage("圈子创建成功");
        return ajaxMessage;
    }

    @Override
    public List<Circle> getAllCircle(long uid) {
        return circleMapper.selectAll(uid);
    }


    private String uploadAvatar(MultipartFile avatar) {
        aliyunOSSProvider.initOssClient();
        String fileUrl = aliyunOSSProvider.uploadImg2Oss(avatar);
        log.info("fileUrl = " + fileUrl);
        return fileUrl;
    }

    @Override
    public District findDistrictById(Integer districtId) {
        return districtMapper.selectById(districtId);
    }



    @Override
    public List<Circle> getAllCircleByDistrict(Integer districtId,long uid) {
        return circleMapper.selectAllByDistrict(districtId,uid);
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Override
    public AjaxMessage joinCircle(long userId, Integer cId) {
        int changeRow = circleUserMapper.UpdateJoinCircle(userId,cId);
        if(changeRow==0) {
            CircleUser circleUser = new CircleUser().build(userId, cId);
            circleUser.setType(3);
            circleUserMapper.insert(circleUser);
            log.info("创建新的帖子成员数据");
        }


        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(userId,cId);
        circleOperationMessage.setMessage("加入圈子");
        circleOperationMessage.setType(2);
        circleOperationMessageMapper.insert(circleOperationMessage);

        circleMapper.joinMember(cId);
        AjaxMessage ajaxMessage = new AjaxMessage();
        ajaxMessage.setSuccess(true);
        ajaxMessage.setMessage("加入成功");

        //恢复在这个圈子的帖子
        int recover = articleMapper.recoverArticle(userId,cId);
        log.info("恢复"+recover+"个帖子。");
        return ajaxMessage;
    }

    @Override
    public List<UserOwnCircle> selectUserOwn(long uid) {
        return circleUserMapper.selectByUid(uid);
    }

    @Override
    public List<User> selectCircleUserByCid(int cid) {
        return circleUserMapper.selectCircleUserByCid(cid);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void quit(long uid, Integer cid) {
        //操作circle_user数据库退出圈子
        circleUserMapper.quit(uid, cid);
        log.info("已退出");
        //记录日志
        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(uid, cid);
        circleOperationMessage.setType(3);
        circleOperationMessage.setMessage("退出圈子");
        circleOperationMessageMapper.insert(circleOperationMessage);
        log.info("日志已记录");
        //圈子的成员数-1
        circleMapper.quitMember(cid);

        //删除帖子
        articleMapper.deleteByQuit(uid,cid);
    }

    @Override
    public List<Circle> getSearchCircle(long uid, String search) {
        search = "%" + search + "%";
        return circleMapper.getSearchCircle(uid,search);
    }

    @Override
    public List<Article> selectHotArticleByCid(Integer cid) {
        List<Article> articles = articleMapper.selectHotArticleByCid(cid);
        return articles;
    }

    @Override
    @Transactional(readOnly = false)
    public void close(long uid, Integer cid) {
        //解散圈子
        circleMapper.close(cid);
        //更改圈子成员状态
        int deleteUserNum = circleUserMapper.close(cid);
        log.info("更改的成员数 = " +deleteUserNum);
        //记录日志
        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(uid, cid);
        circleOperationMessage.setType(8);
        circleOperationMessage.setMessage("解散圈子");
        circleOperationMessageMapper.insert(circleOperationMessage);
        log.info("日志已记录");

        //删除帖子
        int changeRow = articleMapper.deleteByClose(cid);
        log.info("删除帖子数 = "+ changeRow);
    }
}

