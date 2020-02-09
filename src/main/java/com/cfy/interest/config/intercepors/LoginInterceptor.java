package com.cfy.interest.config.intercepors;

import com.cfy.interest.model.User;
import com.cfy.interest.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private IndexService indexService;

    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/signIn").toString();
        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
        HttpSession session = request.getSession();
        //这里的User是登陆时放入session的
        User user = (User) session.getAttribute("user");
        //如果session中没有user，表示没登陆
        log.info("path = "+request.getRequestURL());
        log.info("session 中的user = " + user);
        if (user == null) {
            log.info("session中的user为空，查询cookies");
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
            Cookie[] cookies = request.getCookies();
            //遍历cookies
            if (cookies == null) {
                log.info("cookies为空");
                log.info("跳转到"+tempContextUrl);
                response.sendRedirect(tempContextUrl);
                return false;
            }
            for (Cookie cookie : cookies) {
                //找到名为token的cookie
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    System.out.println(token);
                    //查找用户
                    user = indexService.signInByToken(token);
                    log.info("cookie获取的user = "+user);
                    if (user != null) {
                        log.info("cookie中找到用户");
                        request.getSession().setAttribute("user", user);
                        return true;
                    } else {
                        log.info("cookies中未找到用户");
                        log.info("跳转到"+tempContextUrl);
                        response.sendRedirect(tempContextUrl);
                        return false;
                    }
                }
            }
            log.info("cookie未找到token");
            log.info("跳转到"+tempContextUrl);
            response.sendRedirect(tempContextUrl);
            return false;
        } else {
            return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}


