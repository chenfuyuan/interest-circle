package com.cfy.interest.service.impl;


import com.cfy.interest.mapper.ArticleCommentMapper;
import com.cfy.interest.mapper.ArticleMapper;
import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.model.ArticleComment;
import com.cfy.interest.model.ArticleOperationMessage;
import com.cfy.interest.service.ArticleCommentService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CommentSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {


    @Autowired
    private ArticleCommentMapper articleCommentMapper;


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleOperationMessageMapper articleOperationMessageMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxMessage saveComment(CommentSaveVo commentSaveVo, Long uid) throws Exception {
        int rid = commentSaveVo.getRid();
        int aid = commentSaveVo.getAid();
        String content = commentSaveVo.getContent();

        ArticleComment articleComment = new ArticleComment();
        articleComment.setAid(aid);
        articleComment.setUid(uid);
        articleComment.setContent(content);
        articleComment.setRid(rid);


        articleCommentMapper.insert(articleComment);

        //改变帖子评论数
        int changeRow = articleMapper.comment(aid);
        if (changeRow < 1) {
            throw new Exception("帖子不存在");
        }
        ArticleOperationMessage articleOperationMessage = null;
        //写入日志
        if (rid != 0) {
            articleOperationMessage = new ArticleOperationMessage(uid, aid, ArticleOperationMessage.REPLY);
        } else {
            articleOperationMessage = new ArticleOperationMessage(uid, aid, ArticleOperationMessage.ACOMMENT);
        }

        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "评论成功");
    }
}
