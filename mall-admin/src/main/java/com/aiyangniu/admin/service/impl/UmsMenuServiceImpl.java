package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.UmsMenuMapper;
import com.aiyangniu.admin.service.UmsMenuService;
import com.aiyangniu.entity.model.bo.UmsMenuNode;
import com.aiyangniu.entity.model.pojo.ums.UmsMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理实现类
 *
 * @author lzq
 * @date 2024/02/26
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMenuServiceImpl implements UmsMenuService {

    private final UmsMenuMapper menuMapper;

    @Override
    public int create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        return menuMapper.insert(umsMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            // 没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            // 有父菜单时选择根据父菜单level设置
            UmsMenu parentMenu = menuMapper.selectById(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public int update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        return menuMapper.updateById(umsMenu);
    }

    @Override
    public UmsMenu getItem(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public int delete(Long id) {
        return menuMapper.deleteById(id);
    }

    @Override
    public List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return menuMapper.selectList(new LambdaQueryWrapper<UmsMenu>().eq(UmsMenu::getParentId, parentId).orderByDesc(UmsMenu::getSort));
    }

    @Override
    public List<UmsMenuNode> treeList() {
        List<UmsMenu> umsMenuList = menuMapper.selectList(new QueryWrapper<>());
        List<UmsMenuNode> result = umsMenuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, umsMenuList))
                .collect(Collectors.toList());

//        List<UmsMenu> umsMenuList = menuMapper.selectList(new QueryWrapper<>());
//        List<UmsMenu> mainMenuList = umsMenuList.stream().filter(menu -> menu.getParentId().equals(0L)).collect(Collectors.toList());
//        List<UmsMenuNode> result = new ArrayList<>();
//        for (UmsMenu mainMenu : mainMenuList) {
//            UmsMenuNode umsMenuNode = covertMenuNode(mainMenu, umsMenuList);
//            result.add(umsMenuNode);
//        }

        return result;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return menuMapper.updateById(umsMenu);
    }

    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private UmsMenuNode covertMenuNode(UmsMenu menu, List<UmsMenu> menuList) {
        UmsMenuNode node = new UmsMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<UmsMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);

//        UmsMenuNode node = new UmsMenuNode();
//        BeanUtils.copyProperties(menu, node);
//        List<UmsMenuNode> children = new ArrayList<>();
//        List<UmsMenu> subMenuList = menuList.stream().filter(subMenu -> subMenu.getParentId().equals(menu.getId())).collect(Collectors.toList());
//        for (UmsMenu subMenu : subMenuList) {
//            UmsMenuNode umsMenuNode = covertMenuNode(subMenu, menuList);
//            children.add(umsMenuNode);
//        }
//        node.setChildren(children);

        return node;
    }
}
