package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssignRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 根据用户ID查找该用户的角色信息
     *
     * @param userId 用户ID，用于查询该用户的角色信息
     * @return 返回一个包含用户所有角色和系统所有角色的映射
     */
    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        // 查询系统中的所有角色
        List<SysRole> sysRoles = list();

        // 构建查询条件，查询指定用户ID的所有用户角色关联信息
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(queryWrapper);

        // 提取用户角色关联信息中的角色ID
        List<Long> sysRoleIds = sysUserRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        // 根据角色ID列表查询用户拥有的角色信息
        List<SysRole> sysRolesOfUser = listByIds(sysRoleIds);

        // 构建返回的映射对象，包含用户拥有的角色列表和系统中的所有角色列表
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", sysRolesOfUser);
        roleMap.put("allRolesList", sysRoles);

        // 返回构建的角色信息映射
        return roleMap;
    }

    /**
     * 执行角色分配操作
     * 此方法首先删除用户已有的所有角色关联，然后根据传入的角色ID列表重新分配角色
     * 这是为了确保用户最终只拥有AssignRoleVo中指定的角色权限
     *
     * @param assignRoleVo 包含用户ID和角色ID列表的实体类，用于指定角色分配的信息
     */
    @Override
    @Transactional
    public void doAssign(AssignRoleVo assignRoleVo) {
        // 获取用户ID
        Long sysUserId = assignRoleVo.getUserId();
        // 获取角色ID列表
        List<Long> sysRoleIds = assignRoleVo.getRoleIdList();

        // 创建查询条件，用于删除用户已有的所有角色关联
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, sysUserId);
        // 删除用户已有的角色关联，确保用户的角色权限可以被重新分配
        sysUserRoleService.remove(queryWrapper);

        // 遍历角色ID列表，为用户重新分配角色
        sysRoleIds.forEach(sysRoleId -> {
            // 创建新的用户角色关联对象
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUserId);
            sysUserRole.setRoleId(sysRoleId);

            // 保存新的用户角色关联，实现角色分配
            sysUserRoleService.save(sysUserRole);
        });
    }
}
