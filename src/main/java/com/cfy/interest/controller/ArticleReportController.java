package com.cfy.interest.controller;

import com.cfy.interest.model.User;
import com.cfy.interest.service.ArticleReportService;
import com.cfy.interest.vo.AjaxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ArticleReportController {

    @Autowired
    private ArticleReportService articleReportService;
    @GetMapping("/article/report/{aid}")
    @ResponseBody
    public AjaxMessage report(@PathVariable("aid") Integer aid, @RequestParam("report")Integer report,
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Long uid = user.getId();
        AjaxMessage ajaxMessage = articleReportService.report(aid, report, uid);
        return ajaxMessage;
    }


}
