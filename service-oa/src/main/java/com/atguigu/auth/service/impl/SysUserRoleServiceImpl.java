package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysUserRoleMapper;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysUserRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
