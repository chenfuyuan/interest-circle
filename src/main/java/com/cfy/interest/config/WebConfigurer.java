package com.cfy.interest.config;

import com.cfy.interest.config.intercepors.CircleInterceptor;
import com.cfy.interest.config.intercepors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    private String[] addPathPatterns ={"/**"};
    private String[] excludePathPatterns = {"/static/**", "/signIn/**", "/signUp/**", "/error","/get/**","/check/**"};


    @Autowired
    private LoginInterceptor loginInterceptor;


    @Autowired
    private CircleInterceptor circleInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns("/**") 表示拦截所有的请求
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        registry.addInterceptor(loginInterceptor).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
        registry.addInterceptor(circleInterceptor).addPathPatterns("/circle/admin/**");
    }


}
