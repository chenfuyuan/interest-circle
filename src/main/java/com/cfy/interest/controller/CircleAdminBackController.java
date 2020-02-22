package com.cfy.interest.controller;

import com.cfy.interest.model.Circle;
import com.cfy.interest.service.CircleAdminBackService;
import com.cfy.interest.vo.CircleDayStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class CircleAdminBackController {


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
        model.addAttribute("todayData", circleStatisticsToday);
        model.addAttribute("yesterdayData",circleStatisticsYesterday);
        model.addAttribute("viewType", 1);
        model.addAttribute("circle", circle);

        return "circleBack/admin_back";
    }


}
