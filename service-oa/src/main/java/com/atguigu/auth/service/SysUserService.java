package com.atguigu.auth.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUser> {
    IPage<SysUser> index(Long page, Long limit, SysUserQueryVo sysUserQueryVo);
}
