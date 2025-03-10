package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssignMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/system/sysMenu")
@Api(tags = "系统菜单管理")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;

    @ApiOperation(value = "获取菜单")
    @GetMapping("/findNodes")
    public Result<List<SysMenu>> findNodes() {
        List<SysMenu> sysMenus = sysMenuService.findNodes();
        return Result.ok(sysMenus);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public Result<?> save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("/update")
    public Result<?> updateById(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result<?> remove(@PathVariable Long id) {
        sysMenuService.removeByMenuId(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result<List<SysMenu>> toAssign(@PathVariable Long roleId) {
        List<SysMenu> sysMenus = sysMenuService.getMenuByRoleId(roleId);
        return Result.ok(sysMenus);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result<?> doAssign(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuService.doAssign(assignMenuVo);
        return Result.ok();
    }
}
