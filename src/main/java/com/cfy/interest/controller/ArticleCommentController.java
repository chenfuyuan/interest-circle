package com.cfy.interest.controller;

import com.cfy.interest.model.ArticleComment;
import com.cfy.interest.model.ArticleCommentReply;
import com.cfy.interest.model.ArticleCommentShow;
import com.cfy.interest.model.User;
import com.cfy.interest.service.ArticleCommentService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ArticleCommentReplyVo;
import com.cfy.interest.vo.CommentSaveVo;
import com.cfy.interest.vo.DeleteReplyVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class ArticleCommentController {


    @Autowired
    private ArticleCommentService articleCommentService;

    @PostMapping("/article/comment/save")
    @ResponseBody
    public ArticleComment saveComment(@RequestBody CommentSaveVo commentSaveVo, HttpServletRequest request) {
        log.info("commentSaveVo = " + commentSaveVo);
        User user = getUser(request);
        Long uid = user.getId();
        ArticleComment articleComment = null;
        try {
            articleComment = articleCommentService.saveComment(commentSaveVo, uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return articleComment;
    }

    @GetMapping("/article/comments/get/{aid}")
    @ResponseBody
    public PageInfo<ArticleCommentShow> getComments(@PathVariable("aid") Integer aid,
                                            @RequestParam(name = "pageNum") Integer pageNum
            ,@RequestParam(name="pageSize",defaultValue = "10")Integer pageSize,HttpServletRequest request) {
        if (pageNum ==null||pageNum < 0) {
            pageNum = 0;
        }

        int count = articleCommentService.selectCountByAId(aid);
        if (count == 0 || (pageNum - 1) * pageSize >= count) {
            return null;
        }
        User user = getUser(request);
        long uid = user.getId();
        PageHelper.startPage(pageNum, pageSize,"create_time desc");
        List<ArticleCommentShow> comments = null;
        try {
            comments = articleCommentService.getComments(aid, uid);
            PageInfo<ArticleCommentShow> pageInfo = new PageInfo<>(comments, pageSize);
            return pageInfo;
        }finally {
            PageHelper.clearPage();
        }

    }

    @PostMapping("/article/reply/save")
    @ResponseBody
    public ArticleCommentReply saveReply(@RequestBody ArticleCommentReplyVo articleCommentReplyVo, HttpServletRequest request) {
        User user = getUser(request);
        long uid = user.getId();

        try {
            return articleCommentService.saveReply(articleCommentReplyVo,uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @PostMapping("/article/comment/delete")
    @ResponseBody
    public AjaxMessage deleteComment(@RequestBody DeleteReplyVo deleteReplyVo, HttpServletRequest request) {
        User user = getUser(request);
        long uid = user.getId();
        deleteReplyVo.setUid(uid);
        try {
           return articleCommentService.deleteComment(deleteReplyVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, e.getMessage());
        }


    }

    @PostMapping("/article/reply/delete")
    @ResponseBody
    public AjaxMessage deleteReply(@RequestBody DeleteReplyVo deleteReplyVo, HttpServletRequest request) {
        log.info("deleteReplyVo = " + deleteReplyVo);
        User user = getUser(request);
        Long uid = user.getId();
        try {
            return articleCommentService.deleteReply(deleteReplyVo);

        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, e.getMessage());
        }

    }

    private User getUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return user;
    }
}
