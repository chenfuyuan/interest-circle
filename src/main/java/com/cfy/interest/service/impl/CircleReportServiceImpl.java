package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.CircleOperationMessageMapper;
import com.cfy.interest.mapper.CircleReportMapper;
import com.cfy.interest.model.CircleOperationMessage;
import com.cfy.interest.model.CircleReport;
import com.cfy.interest.service.CircleReportService;
import com.cfy.interest.service.vo.AjaxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CircleReportServiceImpl implements CircleReportService {

    @Autowired
    private CircleReportMapper circleReportMapper;

    @Autowired
    private CircleOperationMessageMapper circleOperationMessageMapper;
    @Transactional(readOnly = false)
    @Override
    public AjaxMessage reportCircle(CircleReport circleReport) {
        //添加举报信息
        circleReport.setState(1);
        circleReportMapper.insert(circleReport);

        long uid = circleReport.getUid();
        int cid = circleReport.getCid();
        String reportMessage = circleReport.getReportMessage();

        //添加圈子操作记录
        CircleOperationMessage circleOperationMessage = new CircleOperationMessage().build(uid, cid);
        circleOperationMessage.setMessage("举报圈子:" + reportMessage);
        circleOperationMessage.setType(4);
//        插入数据库
        circleOperationMessageMapper.insert(circleOperationMessage);

        AjaxMessage ajaxMessage = new AjaxMessage(true, "举报成功");
        return ajaxMessage;
    }
}
