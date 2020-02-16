package com.cfy.interest.controller;

import com.cfy.interest.model.Article;
import com.cfy.interest.model.User;
import com.cfy.interest.service.ArticleService;
import com.cfy.interest.service.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/article/editor")
    public String index(Model model, @RequestParam(name = "cid") Integer cid) {
        model.addAttribute("cid", cid);
        return "article/editor";
    }


    @RequestMapping("/article/upload/image")
    @ResponseBody
    public ArticleUploadImageVo uploadImage(@RequestParam("file") MultipartFile files[]) {
        //判断是否上传的是图片
        log.info(files.length + "");
        for (MultipartFile file : files) {
            log.info("文件名为:" + file.getOriginalFilename());
        }

        //将图片上传到oss服务器中
        List<String> data = articleService.uploadImages(files);
        log.info("上传后的路径为:" + data);
        ArticleUploadImageVo articleUploadImageVo = new ArticleUploadImageVo();
        articleUploadImageVo.setErrno(0);
        articleUploadImageVo.setData(data);

        return articleUploadImageVo;
    }


    @PostMapping("/article/publish")
    @ResponseBody
    public void publish(@RequestBody EditorArticleVo editorArticleVo,
                        HttpServletRequest request) {
        log.info(editorArticleVo.toString());
        //获取User
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();


        articleService.publish(uid, editorArticleVo);
    }


    @GetMapping("/article/sticky/get")
    @ResponseBody
    public List<Article> getSticky(@RequestParam("cid") Integer cid) {
        List<Article> stickys = articleService.getStickys(cid);
        return stickys;
    }

    @PostMapping("article/get")
    @ResponseBody
    public PageInfo<ArticleShow> getArticles(@RequestBody GetArticleVo getArticleVo, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        //分页查询
        Integer pageNum = getArticleVo.getPageNum();
        Integer pageSize = 4;
        log.info("pageNum ==" + pageNum);
        int count = articleService.selectCountByCId(getArticleVo.getCid());
        if (count == 0 || (pageNum - 1) * pageSize >= count) {
            return null;
        }
        String sort = getArticleVo.getSort();
        //分页查询
        PageHelper.startPage(pageNum, pageSize, sort + " desc");
        try {
            List<ArticleShow> articles = articleService.getArticles(getArticleVo, uid);
            log.info(articles.get(0).toString());
            PageInfo<ArticleShow> pageInfo = new PageInfo<ArticleShow>(articles, pageSize);
            log.info("list = " + pageInfo);
            return pageInfo;
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }


    }


    @GetMapping("/article/like/{aid}")
    @ResponseBody
    public AjaxMessage like(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (aid > 0) {
            AjaxMessage ajaxMessage = new AjaxMessage(false, "点赞的帖子不存在");
        }
        long uid = user.getId();
        //点赞操作
        AjaxMessage ajaxMessage = articleService.like(uid, aid);

        return ajaxMessage;
    }


    @GetMapping("/article/like/cancel/{aid}")
    @ResponseBody
    public AjaxMessage cancelLike(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (aid > 0) {
            AjaxMessage ajaxMessage = new AjaxMessage(false, "取消点赞的帖子不存在");
        }
        long uid = user.getId();
        //点赞操作
        AjaxMessage ajaxMessage = articleService.cancelLike(uid, aid);

        return ajaxMessage;
    }

    @GetMapping("/article/sticky/{aid}")
    @ResponseBody
    public Article sticky(@PathVariable("aid") Integer aid,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        Article article = articleService.sticky(uid, aid);
        return article;
    }

    @GetMapping("/article/essence/{aid}")
    @ResponseBody
    public AjaxMessage essence(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        AjaxMessage ajaxMessage = articleService.essence(uid, aid);
        return ajaxMessage;
    }

    @GetMapping("/article/sticky/cancel/{aid}")
    @ResponseBody
    public AjaxMessage cancelSticky(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        AjaxMessage ajaxMessage = articleService.cancelSticky(uid, aid);
        return ajaxMessage;
    }

    @GetMapping("/article/essence/cancel/{aid}")
    @ResponseBody
    public AjaxMessage cancelEssence(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        AjaxMessage ajaxMessage = articleService.cancelEssence(uid, aid);
        return ajaxMessage;
    }

    @GetMapping("/article/star/{aid}")
    @ResponseBody
    public AjaxMessage star(@PathVariable("aid") Integer aid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        AjaxMessage ajaxMessage = articleService.star(uid, aid);
        return ajaxMessage;
    }

    @GetMapping("/article/star/cancel/{aid}")
    @ResponseBody
    public AjaxMessage cancelStar(@PathVariable("aid") Integer aid, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        AjaxMessage ajaxMessage = articleService.cancelStar(uid, aid);
        return ajaxMessage;
    }

    @GetMapping("/article/delete/{aid}")
    @ResponseBody
    public AjaxMessage delete(@PathVariable("aid") Integer aid,@RequestParam("cid")Integer cid,
                              HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Long uid = user.getId();
        AjaxMessage ajaxMessage = null;
        try {
            ajaxMessage = articleService.delete(uid, aid,cid);
        } catch (Exception e) {
            log.info("");
            e.printStackTrace();
            return new AjaxMessage(false, e.getMessage());
        }
        return ajaxMessage;
    }
}