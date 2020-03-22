package com.cfy.interest.controller;

import com.cfy.interest.model.Circle;
import com.cfy.interest.model.CircleUser;
import com.cfy.interest.model.User;
import com.cfy.interest.service.CircleAdminBackService;
import com.cfy.interest.vo.AddAdminVo;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CircleDayStatistics;
import com.cfy.interest.vo.CircleSettingSaveVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class CircleAdminBackController {
    private final static int VIEW_INDEX = 1;
    private final static int VIEW_SETTING = 2;
    private final static int VIEW_LIMIT = 3;

    @Autowired
    private CircleAdminBackService circleAdminBackService;

    @GetMapping("/circle/admin/index")
    public String adminBack(@RequestParam("cid") Integer cid, Model model) {
        Circle circle = circleAdminBackService.selectCircleByCid(cid);
        CircleDayStatistics circleStatisticsToday = circleAdminBackService.circleStatisticsToday(cid);
        CircleDayStatistics circleStatisticsYesterday = circleAdminBackService.circleStatisticsYesterday(cid);
        log.info("today = "+circleStatisticsToday);
        log.info("yesterday = " + circleStatisticsYesterday);
        log.info("cirlce = " + circle);
        //今日数据
        model.addAttribute("todayData", circleStatisticsToday);
        //昨日数据
        model.addAttribute("yesterdayData",circleStatisticsYesterday);
        //界面类型
        model.addAttribute("viewType", VIEW_INDEX);
        //圈子数据
        model.addAttribute("circle", circle);
        return "circleBack/admin_back";
    }


    /**
     * 圈子后台设置界面
     *
     * @param cid
     * @param model
     * @return
     */
    @GetMapping("/circle/admin/setting")
    public String setting(@RequestParam("cid") Integer cid, Model model) {
        Circle circle = circleAdminBackService.selectCircleByCid(cid);
        //圈子数据
        model.addAttribute("circle", circle);
        model.addAttribute("viewType", VIEW_SETTING);
        return "circleBack/admin_back";
    }

    @PostMapping("/circle/setting/save")
    @ResponseBody
    public AjaxMessage settingSave(
            @RequestParam(value = "cid", required = true) Integer cid,
            @RequestParam(value = "circleName", required = false) String name,
            @RequestParam(value = "introduce", required = false) String introduce,
            @RequestParam(required = false, value = "background") MultipartFile background,
            @RequestParam(required = false, value = "avatar") MultipartFile avatar, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Long uid = user.getId();
        /*封装数据*/
        CircleSettingSaveVo vo = new CircleSettingSaveVo();
        vo.setName(name);
        vo.setAvatar(avatar);
        vo.setBackground(background);
        vo.setIntroduce(introduce);
        vo.setCid(cid);
        log.info("vo = " + vo);
        AjaxMessage ajaxMessage = circleAdminBackService.saveSetting(vo, uid);
        return ajaxMessage;
    }


    /**
     * 圈子后台任命管理员界面
     *
     * @param cid
     * @param model
     * @return
     */
    @GetMapping("/circle/admin/limit")
    public String limit(@RequestParam("cid") Integer cid, Model model) {
        Circle circle = circleAdminBackService.selectCircleByCid(cid);
        //获取管理员列表
        List<User> adminList = circleAdminBackService.getAdminUserList(cid);
        log.info("adminList = "+adminList);
        //圈子数据
        model.addAttribute("adminList", adminList);
        model.addAttribute("circle", circle);
        model.addAttribute("viewType", VIEW_LIMIT);
        return "circleBack/admin_back";
    }

    @GetMapping("/circle/back/delete/admin")
    @ResponseBody
    public AjaxMessage deleteAdmin(@RequestParam(name="uid",required = true)Long uid,
                                   @RequestParam(name="cid",required = true)Integer cid,HttpServletRequest request) {
        User user = getUser(request);
        //获取操作人员
        Long operatingUid = user.getId();
        AjaxMessage ajaxMessage = null;
        try {
            ajaxMessage = circleAdminBackService.deleteAdmin(cid, uid, operatingUid);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxMessage = new AjaxMessage(false, e.getMessage());
        }

        return ajaxMessage;


    }

    private User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    @GetMapping("/circle/admin/get/memberList")
    @ResponseBody
    public PageInfo<CircleUser> getMemberList(@RequestParam(name = "cid",required = true)Integer cid,
                                        @RequestParam(name="pageNum",defaultValue = "1")Integer pageNum,
                                        @RequestParam(defaultValue = "5", value = "pageSize")Integer pageSize,
                                        @RequestParam(name = "search",required = true)String search,
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

        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);
        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
            //获取成员列表
            List<CircleUser> memberList = null;
            if (search.equals("")) {
                 memberList= circleAdminBackService.getMemberList(cid);
            } else {
                memberList = circleAdminBackService.getMemberListByName(cid, search);
            }
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo<CircleUser> pageInfo = new PageInfo<>(memberList,pageSize);
            log.info("pageInfo = " + pageInfo);

            log.info("pageInfo.navigateNums = " + Arrays.toString(pageInfo.getNavigatepageNums()));
            return pageInfo;
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
    }

    @GetMapping("/circle/get/adminList")
    @ResponseBody
    public List<User> getAdminList(@RequestParam(name="cid",required = true)Integer cid) {
        return circleAdminBackService.getAdminUserList(cid);
    }

    @PostMapping("/circle/back/add/admin")
    @ResponseBody
    public AjaxMessage addAdmin(@RequestBody AddAdminVo addAdminVo, HttpServletRequest request) {
        log.info("addAdminVo = " + addAdminVo);
        User user = getUser(request);
        Long uid = user.getId();
        AjaxMessage ajaxMessage;
        try {
            ajaxMessage = circleAdminBackService.addAdminList(addAdminVo, uid);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxMessage = new AjaxMessage(false, "操作失败");
        }
            return ajaxMessage;
    }
}
