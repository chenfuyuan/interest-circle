package com.cfy.interest.controller;

import com.cfy.interest.model.Article;
import com.cfy.interest.model.User;
import com.cfy.interest.model.UserOwnCircle;
import com.cfy.interest.service.CircleService;
import com.cfy.interest.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private CircleService circleService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,HttpServletResponse response) {
        return index(request, 1, model,response);
    }
    @GetMapping("/{pageNum}")
    public String index(HttpServletRequest request, @PathVariable(required = false,name="pageNum") Integer pageNum, Model model,HttpServletResponse response) {
        log.info("访问首页");
        int pageSize = 4;
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }

        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();

        //获取用户参与的圈子
        List<UserOwnCircle> userOwnCircles = circleService.selectUserOwn(uid);
        //将用户选择的圈子和用户圈子列表传递到前台

        //如果还未加入圈子，跳转到加入圈子界面
        if(userOwnCircles.isEmpty()){
            return "redirect:/circle/querySearch";
        }

        if (userOwnCircles.size() < pageNum) {
            pageNum = userOwnCircles.size();
        }

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("userOwnCircles", userOwnCircles);


        //获取选中圈子的前4个成员，根据职务顺序取得
        int cid = userOwnCircles.get(pageNum-1).getCid();
        log.info("选中的圈子id = " + cid);
        List<User> userlist = circleService.selectCircleUserByCid(cid);
        log.info("将在首页显示以下成员的头像 "+userlist);
        log.info("多少个 " + userlist.size());
        model.addAttribute("circleMember", userlist);

        //热门帖子
        List<Article> articles = circleService.selectHotArticleByCid(cid);
        model.addAttribute("hotArticles", articles);
        return "index";
    }

    @GetMapping("/logOut")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        //移除session中的user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        indexService.logOut(user.getId());
        return "signin";
    }

    @GetMapping
    public String error() {
        return "error";
    }



}
