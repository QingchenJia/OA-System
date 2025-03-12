package com.atguigu.common.interceptor;

import com.atguigu.common.jwt.JwtUtil;
import com.atguigu.common.jwt.LoginUser;
import com.atguigu.common.jwt.LoginUserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行拦截处理
     *
     * @param request  请求对象，用于获取请求头中的token信息
     * @param response 响应对象，可用于处理拦截后的响应
     * @param handler  处理请求的处理器对象
     * @return boolean 表示是否继续执行其他拦截器和处理器方法；返回true表示继续执行
     * <p>
     * 此方法主要用于解析请求头中的token，并获取登录用户信息，供后续处理使用
     * 它通过JwtUtil工具类解析token获取登录用户信息，并使用LoginUserHolder保存该信息
     * 以便在后续的处理过程中可以方便地访问登录用户信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token信息
        String token = request.getHeader("token");

        // 解析token并获取登录用户信息
        LoginUser loginUser = JwtUtil.parseToken(token);
        // 将登录用户信息保存到LoginUserHolder中，以便后续处理使用
        LoginUserHolder.setLoginUser(loginUser);

        // 表示继续执行其他拦截器和处理器方法
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.removeLoginUser();
    }
}
