package com.cfy.interest.controller;

import com.cfy.interest.model.User;
import com.cfy.interest.service.ArticleCommentService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.CommentSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
}
