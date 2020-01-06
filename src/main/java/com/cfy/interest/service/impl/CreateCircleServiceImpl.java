package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.DistrictMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.model.District;
import com.cfy.interest.service.CreateCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateCircleServiceImpl implements CreateCircleService {

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private CircleMapper circleMapper;

    /**
     * 获取省份列表
     *
     * @return
     */
    @Override
    public List<District> getProvinces() {
        //从数据库中获取省份列表
        return districtMapper.getProvinces();
    }

    /**
     * 获取城市列表
     *
     * @param id
     * @return
     */
    @Override
    public List<District> getCityByProvincesId(int id) {
        return districtMapper.findCityByProvince(id);
    }

    @Override
    public Circle existCircle(String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        return circleMapper.findByName("aaa");

    }


}
