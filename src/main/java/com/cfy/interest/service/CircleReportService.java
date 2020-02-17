package com.cfy.interest.service;

import com.cfy.interest.model.CircleReport;
import com.cfy.interest.vo.AjaxMessage;

public interface CircleReportService {

    AjaxMessage reportCircle(CircleReport circleReport);
}
