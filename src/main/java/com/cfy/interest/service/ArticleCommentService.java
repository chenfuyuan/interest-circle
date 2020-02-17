package com.cfy.interest.service;

import com.cfy.interest.model.ArticleCommentShow;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CommentSaveVo;

import java.util.List;

public interface ArticleCommentService {

    AjaxMessage saveComment(CommentSaveVo commentSaveVo,Long uid) throws Exception;

    List<ArticleCommentShow> getComments(Integer aid, long uid);
}
