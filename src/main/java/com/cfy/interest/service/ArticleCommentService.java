package com.cfy.interest.service;

import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CommentSaveVo;

public interface ArticleCommentService {

    AjaxMessage saveComment(CommentSaveVo commentSaveVo,Long uid) throws Exception;
}
