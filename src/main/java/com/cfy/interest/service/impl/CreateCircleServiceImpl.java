package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.DistrictMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.model.City;
import com.cfy.interest.model.Province;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.CreateCircleService;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.CreateCircleFormVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class CreateCircleServiceImpl implements CreateCircleService {


    @Autowired
    private CircleMapper circleMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AliyunOSSProvider aliyunOSSProvider;

    @Autowired
    private DistrictMapper districtMapper;
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
        provinces = redisTemplate.opsForList().range("provinces",0,-1);

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
        Province province = (Province) redisTemplate.opsForList().index("provinces", id-1);
        if (province != null) {
            System.out.println("从redis中获取citys");
            cities = province.getCitys();
        }else{
            cities = districtMapper.findCityByProvince(id);
        }
        return cities;
    }


    @Override
    public Circle existCircle(String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        return circleMapper.findByName(name);
    }

    @Override
    public AjaxMessage createCircle(CreateCircleFormVo createCircleFormVo, long uid) {
        //上传图片到oss服务器上
        String filePath = uploadAvatar(createCircleFormVo.getAvatar());

        return null;
    }


    private String uploadAvatar(MultipartFile avatar)  {
        aliyunOSSProvider.initOssClient();
        String fileUrl = aliyunOSSProvider.uploadImg2Oss(avatar);
        log.info("fileUrl = " + fileUrl);
        return fileUrl;
    }


}
