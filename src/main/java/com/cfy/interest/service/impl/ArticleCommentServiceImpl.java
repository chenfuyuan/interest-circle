package com.cfy.interest.service.impl;


import com.cfy.interest.mapper.ArticleCommentMapper;
import com.cfy.interest.mapper.ArticleCommentReplyMapper;
import com.cfy.interest.mapper.ArticleMapper;
import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.model.ArticleComment;
import com.cfy.interest.model.ArticleCommentReply;
import com.cfy.interest.model.ArticleCommentShow;
import com.cfy.interest.model.ArticleOperationMessage;
import com.cfy.interest.service.ArticleCommentService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ArticleCommentReplyVo;
import com.cfy.interest.vo.CommentSaveVo;
import com.cfy.interest.vo.DeleteReplyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ArticleCommentServiceImpl implements ArticleCommentService {


    @Autowired
    private ArticleCommentMapper articleCommentMapper;


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleOperationMessageMapper articleOperationMessageMapper;

    @Autowired
    private ArticleCommentReplyMapper articleCommentReplyMapper;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleComment saveComment(CommentSaveVo commentSaveVo, Long uid) throws Exception {
        int rid = commentSaveVo.getRid();
        int aid = commentSaveVo.getAid();
        String content = commentSaveVo.getContent();

        ArticleComment articleComment = new ArticleComment();
        articleComment.setAid(aid);
        articleComment.setUid(uid);
        articleComment.setContent(content);


        articleCommentMapper.insert(articleComment);
        articleComment = articleCommentMapper.selectById(articleComment.getId());
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
        return articleComment;
    }

    @Override
    public List<ArticleCommentShow> getComments(Integer aid, long uid) {
        List<ArticleCommentShow> articleCommentShows = articleCommentMapper.selectByAid(aid);

        return articleCommentShows;
    }

    /**
     * 保存回复
     * @param articleCommentReplyVo
     * @param uid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleCommentReply saveReply(ArticleCommentReplyVo articleCommentReplyVo, Long uid) throws Exception {
        ArticleCommentReply articleCommentReply = new ArticleCommentReply();
        Integer acid = articleCommentReplyVo.getAcid();
        Long ruid = articleCommentReplyVo.getRuid();
        String content = articleCommentReplyVo.getContent();
        Integer type = articleCommentReplyVo.getType();
        Integer aid = articleCommentReplyVo.getAid();

        articleCommentReply.setAcid(acid);
        articleCommentReply.setRuid(ruid);
        articleCommentReply.setContent(content);
        articleCommentReply.setUid(uid);
        articleCommentReply.setType(type);

        articleCommentReplyMapper.insert(articleCommentReply);

        articleCommentReply = articleCommentReplyMapper.selectByAcId(articleCommentReply.getId());
        //更新评论回复数
        int changeRow = articleCommentMapper.reply(acid);
        if (changeRow < 1) {
            throw new Exception("此评论不存在");
        }

        changeRow = articleMapper.reply(aid);
        if (changeRow < 1) {
            throw new Exception("该帖子不存在");
        }

        //插入日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid, aid, ArticleOperationMessage.REPLY);

        articleOperationMessageMapper.insert(articleOperationMessage);
        log.info("reply = " + articleCommentReply);

        return articleCommentReply;
    }

    /**
     * 查询帖子的总评论数
     * @param aid
     * @return
     */
    @Override
    public int selectCountByAId(Integer aid) {
        return articleCommentMapper.selectCountByAid(aid);
    }


    /**
     * 删除评论
     * @param deleteReplyVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxMessage deleteComment(DeleteReplyVo deleteReplyVo) throws Exception {
        Long uid = deleteReplyVo.getUid();
        Integer aid = deleteReplyVo.getAid();
        Integer acid = deleteReplyVo.getAcid();

        //删除评论
        int changeRow = articleCommentMapper.deleteByAcId(acid);

        if (changeRow < 1) {
            throw new Exception("该评论不存在");
        }

        //删除与评论相关的回复
        int deleteNum = articleCommentReplyMapper.deleteByAcid(acid);

        //更新帖子评论数据
        changeRow = articleMapper.deleteComment(aid, deleteNum + 1);
        if (changeRow < 1) {
            throw new Exception("评论与帖子不匹配");
        }


        //记录日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid, aid, ArticleOperationMessage.DELETECOMMENT);
        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "删除评论成功");
    }


    /**
     * 删除评论回复
     * @param deleteReplyVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxMessage deleteReply(DeleteReplyVo deleteReplyVo) throws Exception {
        Long uid = deleteReplyVo.getUid();
        Integer aid = deleteReplyVo.getAid();
        Integer acid = deleteReplyVo.getAcid();
        Integer rid = deleteReplyVo.getRid();

        //删除回复
        int changeRow = articleCommentReplyMapper.deleteByRid(rid);
        if (changeRow != 1) {
            throw new Exception("该回复不存在");
        }

        changeRow = articleCommentMapper.deleteReply(acid);
        if (changeRow != 1) {
            throw new Exception("评论id传入错误");
        }

        changeRow = articleMapper.deleteReply(aid);
        if (changeRow != 1) {
            throw new Exception("帖子id传入错误");
        }

        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid, aid, ArticleOperationMessage.DELETEREPLY);
        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "删除帖子成功");
    }
}
