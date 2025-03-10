package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    /**
     * 用户索引接口的实现方法
     * 该方法根据页面号、页面大小以及用户查询条件来获取用户列表
     *
     * @param page           页面号，表示请求的页面位置
     * @param limit          页面大小，表示每页包含的记录数
     * @param sysUserQueryVo 用户查询条件对象，包含查询所需的条件
     * @return 返回一个IPage对象，包含查询到的用户数据
     */
    @Override
    public IPage<SysUser> index(Long page, Long limit, SysUserQueryVo sysUserQueryVo) {
        // 获取用户查询条件中的关键字
        String keyword = sysUserQueryVo.getKeyword();
        // 获取用户查询条件中的创建时间开始值
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        // 获取用户查询条件中的创建时间结束值
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        // 创建一个Page对象，用于分页查询，传入当前页面号和页面大小
        Page<SysUser> sysUserPage = new Page<>(page, limit);

        // 创建一个LambdaQueryWrapper对象，用于构建查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 根据关键字构建查询条件，如果关键字非空，则名称模糊匹配关键字
        // 根据创建时间开始值构建查询条件，如果开始值非空，则创建时间大于等于开始值
        // 根据创建时间结束值构建查询条件，如果结束值非空，则创建时间小于等于结束值
        queryWrapper.like(StringUtils.isNotBlank(keyword), SysUser::getName, keyword)
                .ge(StringUtils.isNotBlank(createTimeBegin), SysUser::getCreateTime, createTimeBegin)
                .le(StringUtils.isNotBlank(createTimeEnd), SysUser::getCreateTime, createTimeEnd);

        // 执行分页查询，传入Page对象和查询条件
        page(sysUserPage, queryWrapper);

        // 返回查询到的用户数据的Page对象
        return sysUserPage;
    }
}
