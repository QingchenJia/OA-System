package com.atguigu.common.jwt;

public class LoginUserHolder {
    private static final ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        threadLocal.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return threadLocal.get();
    }

    public static void removeLoginUser() {
        threadLocal.remove();
    }
}
