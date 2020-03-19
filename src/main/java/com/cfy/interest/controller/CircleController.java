package com.cfy.interest.controller;

import com.cfy.interest.model.*;
import com.cfy.interest.service.CircleService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CreateCircleFormVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class CircleController {

    @Autowired
    private CircleService circleService;

    /**
     * 封装省份列表，跳转到创建圈子界面
     *
     * @return
     */
    @GetMapping("/circle/create")
    public String index(Model model) {
        List<Province> provinces = circleService.getProvinces();

        model.addAttribute("provinces", provinces);
        return "createCircle";
    }

    /**
     * 从前台传入省份的id，获取相应省份的城市列表
     *
     * @param id
     * @return
     */
    @GetMapping("get/city/{id}")
    @ResponseBody
    public List<City> getCity(@PathVariable("id") int id, Model model) {
        if (id == 0) {
            return null;
        }
        List<City> citys = circleService.getCityByProvincesId(id);
        return citys;
    }

    @PostMapping("/circle/create/check")
    @ResponseBody
    public AjaxMessage create(CreateCircleFormVo createCircleFormVo, HttpServletRequest request) {
        log.info("createCircleFormVo = " + createCircleFormVo);

        //获取创建者id
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            log.error("user=null");
        }
        AjaxMessage ajaxMessage = null;
        ajaxMessage = circleService.createCircle(createCircleFormVo, user.getId());

        return ajaxMessage;
    }


    @GetMapping("/circle/querySearch")
    public String getCircleList(Model model,
                                       @RequestParam(required = false,defaultValue="1",value="pageNum")Integer
                                               pageNum,
                                @RequestParam(defaultValue = "2", value = "pageSize")Integer pageSize,
                                HttpServletRequest request) {
        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        log.info("当前页是："+pageNum+"显示条数是："+pageSize);

        User user = (User) request.getSession().getAttribute("user");

        long uid = user.getId();
        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);
        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
            List<Circle> circles = circleService.getAllCircle(uid);
            System.out.println("分页数据："+circles);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Circle> pageInfo = new PageInfo<Circle>(circles,pageSize);
            model.addAttribute("pageInfo", pageInfo);
            log.info("pageInfo = " + pageInfo);
            log.info("pageInfo.navigateNums = " + Arrays.toString(pageInfo.getNavigatepageNums()));

        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }

        return "querySearch";
    }

    @GetMapping("/circle/querySearchByDId")
    public String getCircleList(Model model,Integer districtId,
                                @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                @RequestParam(defaultValue = "2", value = "pageSize")Integer pageSize,
                                HttpServletRequest request) {
        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        log.info("当前页是："+pageNum+"显示条数是："+pageSize);

        //处理省份信息
        log.info("districtId = "+districtId);
        if (districtId == null || districtId == 0) {
            return getCircleList(model, pageNum, pageSize,request);
        }

        District district = circleService.findDistrictById(districtId);
        if (district.getParent() != null) {
            model.addAttribute("province", district.getParent());
            model.addAttribute("city", district);
        } else {
            model.addAttribute("province", district);
        }

        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);
        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
            List<Circle> circles = circleService.getAllCircleByDistrict(districtId,uid);
            model.addAttribute("districtId",districtId);
            System.out.println("分页数据："+circles);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Circle> pageInfo = new PageInfo<Circle>(circles,pageSize);
            model.addAttribute("pageInfo", pageInfo);
            log.info("pageInfo = " + pageInfo);
            log.info("pageInfo.navigateNums = " +Arrays.toString(pageInfo.getNavigatepageNums()));
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }

        return "querySearch";
    }

    @GetMapping("circle/join/{cId}")
    @ResponseBody
    public AjaxMessage joinCircle(@PathVariable("cId") Integer cId,HttpServletRequest request) {
        if (cId == null || cId == 0) {
            return new AjaxMessage(false, "加入失败");
        }
        //获取用户
        User user = (User) request.getSession().getAttribute("user");
        long userId = user.getId();

        //加入圈子
        AjaxMessage ajaxMessage = circleService.joinCircle(userId,cId);

        return ajaxMessage;
    }

    @GetMapping("/circle/quit/{id}")
    public String quitCircle(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        circleService.quit(uid, id);
        return "redirect:/";
    }

    @GetMapping("/circle/search")
    public String search(Model model,
                         @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer
                                 pageNum, @RequestParam(defaultValue = "2", value = "pageSize") Integer pageSize,
                         HttpServletRequest request, @RequestParam(value = "search") String search) {

        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        log.info("当前页是："+pageNum+"显示条数是："+pageSize);

        User user = (User) request.getSession().getAttribute("user");

        long uid = user.getId();
        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);
        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
            List<Circle> circles = circleService.getSearchCircle(uid,search);
            System.out.println("分页数据："+circles);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<Circle> pageInfo = new PageInfo<Circle>(circles,pageSize);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("search", search);
            log.info("pageInfo = " + pageInfo);
            log.info("pageInfo.navigateNums = " +Arrays.toString(pageInfo.getNavigatepageNums()));
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }


        return "circleSearch";
    }


}
