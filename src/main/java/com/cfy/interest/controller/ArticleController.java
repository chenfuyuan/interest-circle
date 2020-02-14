package com.cfy.interest.controller;

import com.cfy.interest.model.Article;
import com.cfy.interest.model.ArticleSticky;
import com.cfy.interest.model.User;
import com.cfy.interest.service.ArticleService;
import com.cfy.interest.service.vo.ArticleUploadImageVo;
import com.cfy.interest.service.vo.EditorArticleVo;
import com.cfy.interest.service.vo.GetArticleVo;
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
    public String index(Model model,@RequestParam(name="cid")Integer cid) {
        model.addAttribute("cid", cid);
        return "article/editor";
    }


    @RequestMapping("/article/upload/image")
    @ResponseBody
    public ArticleUploadImageVo uploadImage(@RequestParam("file") MultipartFile files[]) {
        //判断是否上传的是图片
        log.info(files.length+"");
        for (MultipartFile file : files) {
            log.info("文件名为:" + file.getOriginalFilename());
        }

        //将图片上传到oss服务器中
        List<String> data = articleService.uploadImages(files);
        log.info("上传后的路径为:"+data);
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
    public List<ArticleSticky> getSticky(@RequestParam("cid") Integer cid) {
        List<ArticleSticky> stickys = articleService.getStickys(cid);
        return stickys;
    }

    @PostMapping("article/get")
    @ResponseBody
    public PageInfo<Article> getArticles(@RequestBody GetArticleVo getArticleVo) {
        //分页查询
        int pageNum = getArticleVo.getPageNum();
        int pageSize = 5;


        //分页查询
        PageHelper.startPage(pageNum,pageSize);
        try{
            List<Article> articles = articleService.getArticles(getArticleVo);
            PageInfo<Article> pageInfo = new PageInfo<>(articles, pageSize);
            log.info("list = "+pageInfo.getList().toString());

            return pageInfo;
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }


    }
}