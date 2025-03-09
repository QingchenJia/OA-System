package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result<IPage<SysUser>> index(@PathVariable Long page, @PathVariable Long limit, SysUserQueryVo sysUserQueryVo) {
        IPage<SysUser> sysUserIPage = sysUserService.index(page, limit, sysUserQueryVo);
        return Result.ok(sysUserIPage);
    }
}