package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssignMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findNodes();

    void removeByMenuId(Long id);

    List<SysMenu> getMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);
}
