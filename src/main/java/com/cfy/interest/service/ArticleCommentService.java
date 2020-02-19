package com.cfy.interest.service;

import com.cfy.interest.model.ArticleComment;
import com.cfy.interest.model.ArticleCommentReply;
import com.cfy.interest.model.ArticleCommentShow;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ArticleCommentReplyVo;
import com.cfy.interest.vo.CommentSaveVo;
import com.cfy.interest.vo.DeleteReplyVo;

import java.util.List;

public interface ArticleCommentService {

    ArticleComment saveComment(CommentSaveVo commentSaveVo, Long uid) throws Exception;

    List<ArticleCommentShow> getComments(Integer aid, long uid);

    ArticleCommentReply saveReply(ArticleCommentReplyVo articleCommentReplyVo, Long uid) throws Exception;

    int selectCountByAId(Integer aid);

    AjaxMessage deleteComment(DeleteReplyVo deleteReplyVo) throws Exception;

    AjaxMessage deleteReply(DeleteReplyVo deleteReplyVo) throws Exception;
}
