package com.cfy.interest.service;

import com.cfy.interest.model.Circle;
import com.cfy.interest.model.City;
import com.cfy.interest.model.Province;

import java.util.List;

public interface CreateCircleService {

    /**
     * 从数据库中获取所有省份
     * @return
     */
    List<Province> getProvinces();

    /**
     * 从数据库中根据省份id获取城市列表
     * @return
     */
    List<City> getCityByProvincesId(int id);

    /**
     * 根据名字判断圈子是否存在
     * @param name
     * @return
     */
    Circle existCircle(String name);


}
