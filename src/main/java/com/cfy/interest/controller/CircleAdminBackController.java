package com.cfy.interest.controller;

import com.cfy.interest.model.Circle;
import com.cfy.interest.model.User;
import com.cfy.interest.service.CircleAdminBackService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CircleDayStatistics;
import com.cfy.interest.vo.CircleSettingSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
        //圈子数据
        model.addAttribute("circle", circle);
        model.addAttribute("viewType", VIEW_LIMIT);
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
}
