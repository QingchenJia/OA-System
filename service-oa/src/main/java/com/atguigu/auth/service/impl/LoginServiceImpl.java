package com.atguigu.auth.service.impl;

import com.atguigu.auth.service.LoginService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.exception.CustomException;
import com.atguigu.common.jwt.JwtUtil;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private SysUserService sysUserService;

    /**
     * 用户登录方法
     * <p>
     * 该方法接收一个LoginVo对象作为参数，其中包含用户输入的用户名和密码
     * 它负责验证用户身份，并在验证成功后生成并返回一个JWT令牌
     *
     * @param loginVo 包含用户名和密码的登录视图对象
     * @return 登录成功后返回JWT令牌
     * @throws CustomException 如果用户不存在、账户已停用或密码错误，则抛出自定义异常
     */
    @Override
    public String login(LoginVo loginVo) {
        // 获取用户输入的用户名和密码
        String username = loginVo.getUsername();
        // 使用MD5对密码进行加密处理，以提高安全性
        String password = DigestUtils.md5Hex(loginVo.getPassword());

        // 创建查询条件，根据用户名查询用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        // 执行查询，获取用户信息
        SysUser sysUser = sysUserService.getOne(queryWrapper);

        // 如果用户信息为空，说明用户不存在
        if (sysUser == null) {
            throw new CustomException("当前用户不存在");
        }

        // 如果用户状态为0，表示账户已停用
        if (sysUser.getStatus() == 0) {
            throw new CustomException("当前账户已停用");
        }

        // 比较用户输入的密码（已加密）和数据库中的密码是否一致
        if (!sysUser.getPassword().equals(password)) {
            throw new CustomException("密码输入错误");
        }

        // 登录成功后，生成并返回JWT令牌
        return JwtUtil.generateToken(sysUser.getId(), username);
    }
}
