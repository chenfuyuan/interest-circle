package com.cfy.interest.service;

import com.cfy.interest.vo.AjaxMessage;

public interface ArticleReportService {

    AjaxMessage report(Integer aid, Integer report, Long uid,Integer cid);
}
