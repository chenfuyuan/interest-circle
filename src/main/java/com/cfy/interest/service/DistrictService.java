package com.cfy.interest.service;

import com.cfy.interest.model.City;
import com.cfy.interest.model.Province;

import java.util.List;

public interface DistrictService {
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
}
