package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysMenuMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
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
