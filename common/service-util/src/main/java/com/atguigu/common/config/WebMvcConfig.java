package com.atguigu.common.config;

import com.atguigu.common.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 配置拦截器
     * <p>
     * 该方法用于向Spring MVC的InterceptorRegistry中添加拦截器，以定义哪些请求需要被拦截处理
     * 主要目的是配置登录拦截器，确保除登录请求外的所有请求都被拦截检查
     *
     * @param registry InterceptorRegistry实例，用于注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器，并指定其拦截的路径模式和排除的路径模式
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/**/login",
                        "/doc.html",
                        "/webjars/**",
                        "/v2/api-docs/**",
                        "/swagger-resources/**",
                        "/favicon.ico"
                ); // 但不包括任何路径下的login请求
    }
}
