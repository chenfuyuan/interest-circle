package com.cfy.interest.config.intercepors;

import com.cfy.interest.model.User;
import com.cfy.interest.service.CircleAdminBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class CircleInterceptor implements HandlerInterceptor {

    @Autowired
    private CircleAdminBackService circleAdminBackService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取
        log.info("访问圈子后台的path = " + request.getRequestURL());
        Integer cid =  Integer.parseInt(request.getParameter("cid"));
        log.info("cid = " + cid);
        User user = (User) request.getSession().getAttribute("user");
        long uid = user.getId();
        boolean login = circleAdminBackService.login(uid, cid);
        if (login) {
            log.info("登录用户匹配");
            return true;
        } else {
            StringBuffer url = request.getRequestURL();
            String tempContextUrl =
                    url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/error").toString();
            log.info("跳转到"+tempContextUrl);
            response.sendRedirect(tempContextUrl);
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
