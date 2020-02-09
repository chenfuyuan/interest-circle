package com.cfy.interest.controller;

import com.cfy.interest.model.Province;
import com.cfy.interest.service.DistrictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("/get/provinces")
    @ResponseBody
    public List<Province> getProvinces() {
        List<Province> provinces = districtService.getProvinces();
        log.info(provinces.size()+"");
        return provinces;
    }
}
