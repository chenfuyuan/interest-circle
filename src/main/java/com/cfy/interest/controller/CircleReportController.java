package com.cfy.interest.controller;


import com.cfy.interest.model.CircleReport;
import com.cfy.interest.model.User;
import com.cfy.interest.service.CircleReportService;
import com.cfy.interest.service.vo.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CircleReportController {

    @Autowired
    private CircleReportService circleReportService;

    @GetMapping("/circle/report/{cid}")
    @ResponseBody
    public AjaxMessage reportCircle(@PathVariable("cid")Integer cid, Integer report, HttpServletRequest request) {
//        获取举报人
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();

        CircleReport circleReport = new CircleReport();
        circleReport.setCid(cid);
        circleReport.setUid(uid);
        circleReport.setType(report);

        AjaxMessage ajaxMessage = circleReportService.reportCircle(circleReport);
        return ajaxMessage;

    }
}
