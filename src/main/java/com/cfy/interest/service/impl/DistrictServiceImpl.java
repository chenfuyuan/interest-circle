package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.DistrictMapper;
import com.cfy.interest.model.City;
import com.cfy.interest.model.Province;
import com.cfy.interest.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {


    @Autowired
    private RedisTemplate redisTemplate;

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


}
