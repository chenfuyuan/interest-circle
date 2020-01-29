package com.cfy.interest.controller;

import com.cfy.interest.model.City;
import com.cfy.interest.model.Province;
import com.cfy.interest.model.User;
import com.cfy.interest.service.CreateCircleService;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.CreateCircleFormVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class CreateCircleController {

    @Autowired
    private CreateCircleService createCircleService;
    /**
     * 封装省份列表，跳转到创建圈子界面
     * @return
     */
    @GetMapping("/createCircle")
    public String index(Model model) {
        List<Province> provinces = createCircleService.getProvinces();
        for (Province province : provinces) {
            System.out.println(province);
        }
        model.addAttribute("provinces", provinces);
        return "createCircle";
    }

    /**
     * 从前台传入省份的id，获取相应省份的城市列表
     * @param id
     * @return
     */
    @GetMapping("getCity/{id}")
    @ResponseBody
    public List<City> getCity(@PathVariable("id")int id, Model model) {
        if (id == 0) {
            return null;
        }
        List<City> citys = createCircleService.getCityByProvincesId(id);
        return citys;
    }

    @PostMapping("/createCircle")
    @ResponseBody
    public AjaxMessage create(CreateCircleFormVo createCircleFormVo, HttpServletRequest request){
        log.info("createCircleFormVo = "+createCircleFormVo);

        //获取创建者id
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            log.error("user=null");
        }
        AjaxMessage ajaxMessage =null;

        ajaxMessage = createCircleService.createCircle(createCircleFormVo,user.getId());

        return ajaxMessage;
    }



    



}
