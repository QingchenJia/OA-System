package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysMenuMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleMenuService;
import com.atguigu.common.exception.CustomException;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.vo.system.AssignMenuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 根据系统菜单数据生成菜单树
     * <p>
     * 此方法首先获取所有系统菜单数据，然后构建一个菜单树结构
     * 菜单树结构有助于直观地展示菜单项之间的层级关系
     *
     * @return 菜单树列表
     */
    @Override
    public List<SysMenu> findNodes() {
        // 获取所有系统菜单数据
        List<SysMenu> sysMenus = list();
        // 初始化一个空的列表，用于存放构建好的菜单树
        List<SysMenu> menuTree = new ArrayList<>();

        // 调用generateMenuTree方法生成菜单树并返回
        return generateMenuTree(menuTree, sysMenus);
    }

    /**
     * 根据菜单ID删除菜单及其关联信息
     * <p>
     * 此方法首先检查给定菜单ID的菜单是否包含子菜单如果包含子菜单，则抛出异常，防止层级断裂
     * 如果没有子菜单，则直接调用删除方法，移除该菜单项
     *
     * @param id 要删除的菜单的ID
     * @throws CustomException 如果当前菜单层级包含子菜单时抛出
     */
    @Override
    public void removeByMenuId(Long id) {
        // 创建查询条件，检查是否存在子菜单
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId, id);
        // 执行查询，获取子菜单数量
        int count = count(queryWrapper);

        // 如果存在子菜单，抛出异常，不允许删除
        if (count > 0) {
            throw new CustomException("当前菜单层级包含子菜单");
        }

        // 无子菜单，安全删除
        removeById(id);
    }

    /**
     * 根据角色ID获取菜单列表
     *
     * @param roleId 角色ID，用于查询该角色所拥有的菜单
     * @return 返回该角色的菜单树列表
     */
    @Override
    public List<SysMenu> getMenuByRoleId(Long roleId) {
        // 创建查询条件，只获取状态为1（启用）的菜单
        LambdaQueryWrapper<SysMenu> menuQueryWrapper = new LambdaQueryWrapper<>();
        menuQueryWrapper.eq(SysMenu::getStatus, 1);

        // 执行查询，获取所有启用的菜单
        List<SysMenu> sysMenus = list(menuQueryWrapper);
        // 初始化用于存储最终菜单树的列表
        List<SysMenu> menuTree = new ArrayList<>();

        // 创建查询条件，获取指定角色ID的所有角色菜单关联信息
        LambdaQueryWrapper<SysRoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.list(roleMenuQueryWrapper);

        // 从角色菜单关联信息中提取菜单ID列表
        List<Long> sysMenuIds = sysRoleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());

        // 遍历所有菜单，设置菜单的选中状态
        sysMenus.forEach(sysMenu -> sysMenu.setSelect(sysMenuIds.contains(sysMenu.getId())));

        // 根据处理后的菜单列表生成菜单树，并返回
        return generateMenuTree(menuTree, sysMenus);
    }

    /**
     * 分配菜单项给角色
     * 此方法首先删除角色已有的所有菜单项，然后根据新的分配列表重新插入
     * 使用了事务注解，确保操作的原子性
     *
     * @param assignMenuVo 包含角色ID和菜单项ID列表的封装对象
     */
    @Override
    @Transactional
    public void doAssign(AssignMenuVo assignMenuVo) {
        // 获取角色ID
        Long roleId = assignMenuVo.getRoleId();
        // 获取菜单项ID列表
        List<Long> menuIdList = assignMenuVo.getMenuIdList();

        // 创建查询条件，用于删除角色已有的所有菜单项
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        // 删除角色已有的所有菜单项
        sysRoleMenuService.remove(queryWrapper);

        // 将新的菜单项ID列表转换为SysRoleMenu对象列表
        List<SysRoleMenu> sysRoleMenus = menuIdList.stream()
                .map(menuId -> {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(roleId);
                    sysRoleMenu.setMenuId(menuId);

                    return sysRoleMenu;
                })
                .collect(Collectors.toList());

        // 批量插入新的菜单项
        sysRoleMenuService.saveBatch(sysRoleMenus);
    }

    /**
     * 获取指定菜单节点的所有子节点
     *
     * @param menuTreeNode 当前菜单节点，用于比较其他菜单的父节点
     * @param sysMenus     系统菜单列表，作为查找子节点的源数据
     * @return 返回当前菜单节点的所有子节点列表
     */
    public List<SysMenu> getChildrenNodes(SysMenu menuTreeNode, List<SysMenu> sysMenus) {
        // 初始化一个空的菜单列表，用于存储当前菜单节点的所有子节点
        List<SysMenu> childrenNodes = new ArrayList<>();

        // 遍历系统菜单列表，寻找与当前菜单节点匹配的子节点
        for (SysMenu sysMenu : sysMenus) {
            // 如果某个菜单的父ID与当前菜单节点的ID相等，则说明它是当前节点的子节点
            if (sysMenu.getParentId().equals(menuTreeNode.getId())) {
                // 将找到的子节点添加到子节点列表中
                childrenNodes.add(sysMenu);
            }
        }

        // 返回当前菜单节点的所有子节点列表
        return childrenNodes;
    }

    /**
     * 生成菜单树
     *
     * @param menuTree 菜单树列表，用于存储构建好的菜单树
     * @param sysMenus 系统菜单列表，包含所有菜单项
     * @return 返回构建好的菜单树列表
     * <p>
     * 该方法主要用于根据所有菜单项生成一个菜单树结构，首先找到所有顶级菜单，
     * 然后递归地为每个菜单找到其子菜单，直到所有菜单都被正确地分配到菜单树中
     */
    public List<SysMenu> generateMenuTree(List<SysMenu> menuTree, List<SysMenu> sysMenus) {
        // 如果菜单树为空，则需要初始化菜单树，找到所有顶级菜单
        if (menuTree.isEmpty()) {
            for (SysMenu sysMenu : sysMenus) {
                // 如果菜单的父ID为0，则说明它是顶级菜单
                if (sysMenu.getParentId() == 0) {
                    menuTree.add(sysMenu);
                }
            }
        }

        // 遍历菜单树中的每个菜单项，为它们找到子菜单
        for (SysMenu node : menuTree) {
            // 获取当前菜单项的所有子菜单
            List<SysMenu> childrenNodes = getChildrenNodes(node, sysMenus);

            // 如果当前菜单项有子菜单，则将其设置到当前菜单项的子菜单列表中，并继续递归构建子菜单的菜单树
            if (!childrenNodes.isEmpty()) {
                node.setChildren(childrenNodes);
                generateMenuTree(childrenNodes, sysMenus);
            }
        }

        // 返回构建好的菜单树
        return menuTree;
    }
}
