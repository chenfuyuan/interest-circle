package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.mapper.ArticleReportMapper;
import com.cfy.interest.model.ArticleOperationMessage;
import com.cfy.interest.model.ArticleReport;
import com.cfy.interest.service.ArticleReportService;
import com.cfy.interest.vo.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleReortServiceImpl implements ArticleReportService {

    @Autowired
    private ArticleOperationMessageMapper articleOperationMessageMapper;

    @Autowired
    private ArticleReportMapper articleReportMapper;

    @Transactional
    @Override
    public AjaxMessage report(Integer aid, Integer report, Long uid) {
        //判断是否已经举报
        ArticleReport result = articleReportMapper.selectByUidAndAid(uid, aid);
        if (result != null) {
            return new AjaxMessage(false, "您已经举报过，我们会尽快处理");
        }

        ArticleReport articleReport = new ArticleReport(uid, aid, report);
        articleReportMapper.insert(articleReport);

        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,ArticleOperationMessage.REPORT);
        articleOperationMessage.setMessage(articleOperationMessage.getMessage()+":"+articleReport.getReportMessage());
        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "举报成功");
    }
}
