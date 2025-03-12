package com.atguigu.common.jwt;

import com.atguigu.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final SecretKey secretKey = Keys.hmacShaKeyFor("1234567890abcdefghijklmnopqrstuv".getBytes());

    /**
     * 生成JWT令牌
     * 该方法用于创建一个JSON Web Token (JWT)，其中包含用户信息，如用户ID和用户名
     * JWT用于在用户登录后传递用户身份信息，并可以用于后续请求的授权验证
     *
     * @param id       用户ID，用于标识用户
     * @param username 用户名，用户的名称标识
     * @return 返回生成的JWT令牌字符串
     */
    public static String generateToken(Long id, String username) {
        // 创建一个HashMap用于存储JWT的自定义声明
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);

        // 使用Jwts.builder()开始构建JWT
        // .subject("LOGIN_USER") 设置JWT的主题，这里用于标识登录用户
        // .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60)) 设置JWT的过期时间，这里设置为1小时后
        // .claims(map) 向JWT中添加自定义声明，包含用户信息
        // .signWith(secretKey) 使用密钥对JWT进行签名，确保JWT的安全性和完整性
        // .compact() 将JWT压缩为字符串形式并返回
        return Jwts.builder()
                .subject("LOGIN_USER")
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .claims(map)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析令牌信息
     * 该方法使用JWT解析工具来验证和解析传入的令牌，提取出用户信息，并封装到LoginUser对象中返回
     *
     * @param token 待解析的JWT令牌字符串，包含了用户的身份信息
     * @return LoginUser对象，其中包含了从令牌中解析出的用户ID和用户名
     */
    public static LoginUser parseToken(String token) {
        // 检查令牌是否为空，如果为空则抛出异常
        if (StringUtils.isBlank(token)) {
            throw new CustomException("token不能为空");
        }

        // 创建JWT解析器，并使用预定义的密钥进行签名验证
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        Claims claims = null;
        try {
            // 使用JWT解析器解析传入的令牌，并获取其中的声明（claims）
            claims = jwtParser.parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            // 如果解析过程中出现异常，说明令牌无效，抛出运行时异常
            throw new RuntimeException("token无效");
        }

        // 从声明中获取用户ID和用户名，并将其转换为相应的类型
        Long id = Long.valueOf(claims.get("id").toString());
        String username = (String) claims.get("username");

        // 创建一个新的LoginUser对象，并将从令牌中解析出的用户信息设置到该对象中
        LoginUser loginUser = new LoginUser();
        loginUser.setId(id);
        loginUser.setUsername(username);

        // 返回封装了用户信息的LoginUser对象
        return loginUser;
    }

    public static void main(String[] args) {
        String token = generateToken(1L, "Alice");
        System.out.println(token);

        LoginUser loginUser = parseToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMT0dJTl9VU0VSIiwiZXhwIjoxNzczMTUyMTkxLCJpZCI6MSwidXNlcm5hbWUiOiJBbGljZSJ9.erKrNdlHbHzQ3z6PsQh1UaWaYJydT_9qWCD45Rh-Zdw");
        System.out.println(loginUser);
    }
}
