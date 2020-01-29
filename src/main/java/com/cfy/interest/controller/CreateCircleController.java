package com.cfy.interest.controller;

import com.cfy.interest.model.District;
import com.cfy.interest.model.User;
import com.cfy.interest.service.CreateCircleService;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CreateCircleController {

    @Autowired
    private CreateCircleService createCircleService;
    /**
     * 封装省份列表，跳转到创建圈子界面
     * @return
     */
    @GetMapping("/createCircle")
    public String index(Model model) {
        List<District> provinces = createCircleService.getProvinces();
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
    public List<District> getCity(@PathVariable("id")int id,Model model) {
        if (id == 0) {
            return null;
        }
        List<District> citys = createCircleService.getCityByProvincesId(id);
        return citys;
    }

    @PostMapping("/create")
    public String create(@RequestBody Circle circle, HttpServletRequest request){
        //获取创建者id
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user);

        return "/index";
    }

    



}
