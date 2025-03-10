package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/system/sysUser")
@Api(tags = "系统用户管理")
@ApiSupport
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result<IPage<SysUser>> index(@PathVariable Long page, @PathVariable Long limit, SysUserQueryVo sysUserQueryVo) {
        IPage<SysUser> sysUserIPage = sysUserService.index(page, limit, sysUserQueryVo);
        return Result.ok(sysUserIPage);
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("/get/{id}")
    public Result<SysUser> get(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("/save")
    public Result<?> save(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("/update")
    public Result<?> updateById(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result<?> remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }
}
