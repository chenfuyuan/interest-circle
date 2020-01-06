package com.cfy.interest;

import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.mapper.DistrictMapper;
import com.cfy.interest.model.Circle;
import com.cfy.interest.model.District;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CreateCircleTest {

    @Autowired
    DistrictMapper districtMapper;

    /**
     * 测试获取省份列表
     */
    @Test
    public void testGetProvinces() {
        List<District> provinces = districtMapper.getProvinces();
        System.out.println(provinces);
        //成功
    }


    @Test
    public void testFindCityByProvinces() {
        List<District> citys = districtMapper.findCityByProvince(1);
        for (District district : citys) {
            System.out.println(district);
        }

    }

    @Autowired
    private CircleMapper circleMapper;
    @Test
    public void testCircleFindByName() {
        Circle circle = circleMapper.findByName("aaa");
        System.out.println(circle);
    }
}
