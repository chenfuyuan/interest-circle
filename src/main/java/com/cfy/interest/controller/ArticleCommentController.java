package com.cfy.interest.controller;

import com.cfy.interest.model.ArticleCommentShow;
import com.cfy.interest.model.User;
import com.cfy.interest.service.ArticleCommentService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CommentSaveVo;
import com.github.pagehelper.PageHelper;
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
    public AjaxMessage saveComment(@RequestBody CommentSaveVo commentSaveVo, HttpServletRequest request) {
        log.info("commentSaveVo = " + commentSaveVo);
        User user = (User) request.getSession().getAttribute("user");
        Long uid = user.getId();
        AjaxMessage ajaxMessage = null;
        try {
            ajaxMessage = articleCommentService.saveComment(commentSaveVo, uid);
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxMessage(false, e.getMessage());
        }

        return ajaxMessage;
    }

    @GetMapping("/article/comments/get/{aid}")
    @ResponseBody
    public List<ArticleCommentShow> getComments(@PathVariable("aid") Integer aid,
                                            @RequestParam(name = "pageNum") Integer pageNum
            ,@RequestParam(name="pageSize",defaultValue = "4")Integer pageSize,HttpServletRequest request) {
        if (pageNum ==null||pageNum < 0) {
            pageNum = 0;
        }
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        PageHelper.startPage(pageNum, pageSize,"create_time asc");
        List<ArticleCommentShow> comments = null;
        try {
            comments = articleCommentService.getComments(aid, uid);
        }finally {
            PageHelper.clearPage();
        }


        return comments;
    }
}
