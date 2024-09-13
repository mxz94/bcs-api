package cn.bcs.system.service;

import cn.bcs.common.constant.Constants;
import cn.bcs.common.constant.UserConstants;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysMenu;
import cn.bcs.common.core.domain.entity.SysRole;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.common.enums.SysCommonStatus;
import cn.bcs.system.domain.SysRoleMenu;
import cn.bcs.system.domain.dto.SysRoleMenuDTO;
import cn.bcs.system.domain.vo.SysRoleTreeVO;
import cn.bcs.system.mapper.SysRoleMapper;
import cn.bcs.system.mapper.SysUserMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色业务层
 *
 * @author ruoyi
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @SuppressWarnings("unchecked")
    public TableDataInfo<SysRole> selectRoleList(SysRole role, LoginUser loginUser) {
        List<SysRole> list = this.lambdaQuery()
                .eq(SysRole::getTenantId, UserConstants.SUPER_ADMIN_USER_ID.equals(loginUser.getUserId()) ? 0 : loginUser.getTenantId())
                .like(StrUtil.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())
                .eq(StrUtil.isNotBlank(role.getStatus()), SysRole::getStatus, role.getStatus())
                .like(StrUtil.isNotBlank(role.getRemark()), SysRole::getRemark, role.getRemark())
                .orderByAsc(SysRole::getRoleId).list();
        return TableDataInfo.ok(list);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public Result<Object> insertRole(SysRole role, LoginUser loginUser) {
        role.setTenantId(role.getTenantId() == null ? loginUser.getTenantId() : role.getTenantId());
        role.setRoleKey(role.getTenantId() + "_" + IdUtil.nanoId(5));
        role.setStatus(SysCommonStatus.NORMAL.getCode());
        role.setDelFlag(SysCommonStatus.NORMAL.getCode());
        boolean save = this.save(role);
        return save ? Result.success() : Result.error();
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public Result<Object> updateRole(SysRole role) {
        SysRole sysRole = this.getById(role.getRoleId());
        if (BeanUtil.isEmpty(sysRole)) {
            return Result.error("角色不存在");
        }
        if (Constants.TENANT_ADMIN_ROLE_KEY.equals(sysRole.getRoleKey())) {
            return Result.error("管理员角色不允许修改");
        }
        boolean update = this.updateById(role);
        return update ? Result.success() : Result.error();
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    public Result<Object> updateRoleStatus(SysRole role) {
        SysRole sysRole = this.getById(role.getRoleId());
        if (BeanUtil.isEmpty(sysRole)) {
            return Result.error("角色不存在");
        }
        if (Constants.TENANT_ADMIN_ROLE_KEY.equals(sysRole.getRoleKey())) {
            return Result.error("管理员角色不允许修改");
        }
        sysRole.setStatus(role.getStatus());
        boolean update = this.updateById(sysRole);
        return update ? Result.success() : Result.error();
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> deleteRoleByIds(Long[] roleIds) {
        List<SysRole> roleList = this.listByIds(Arrays.asList(roleIds));
        for (SysRole role : roleList) {
            if (Constants.TENANT_ADMIN_ROLE_KEY.equals(role.getRoleKey())) {
                return Result.error("管理员角色不允许修改");
            }
            Integer count = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getRoleId, role.getRoleId())
                    .eq(SysUser::getDelFlag, SysCommonStatus.NORMAL.getCode()));
            if (count > 0) {
                return Result.error(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        boolean remove = this.removeByIds(Arrays.asList(roleIds));
        if (remove) {
            sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, Arrays.asList(roleIds)));
        }
        return remove ? Result.success() : Result.error();
    }

    /**
     * 角色权限树
     *
     * @param roleId 角色id
     * @return 结果
     */
    public Result<SysRoleTreeVO> menuTree(Long roleId, LoginUser loginUser) {
        SysRoleTreeVO vo = new SysRoleTreeVO();
        if (roleId != null) {
            List<SysRoleMenu> list = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list();
            if (CollUtil.isNotEmpty(list)) {
                List<Long> collect = list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                vo.setCheckedKeys(collect);
            }
        }
        //TODO 改造菜单表后修改这里
        List<SysMenu> sysMenuList = new ArrayList<>();
        if (UserConstants.SUPER_ADMIN_USER_ID.equals(loginUser.getUserId())) {
            //TODO 这里需要查询全部的菜单
            sysMenuList = sysMenuService.selectMenuList(loginUser.getUserId());
        } else {
            //非管理员查询当前用户的角色菜单
            List<SysRoleMenu> adminRoleList = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, loginUser.getUser().getRoleId()).list();
            List<Long> menuIdList = adminRoleList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            sysMenuList = sysMenuService.listByIds(menuIdList);
        }
        List<TreeNode<Integer>> nodeList = sysMenuList.stream()
                .map(e -> new TreeNode<>(e.getMenuId().intValue(),
                        e.getParentId().intValue(),
                        e.getMenuName() + (UserConstants.TYPE_BUTTON.equals(e.getMenuType()) ? "-按钮" : UserConstants.TYPE_MENU.equals(e.getMenuType()) ? "-页面" : ""),
                        e.getOrderNum()))
                .collect(Collectors.toList());
        vo.setMenus(TreeUtil.build(nodeList));
        return Result.success(vo);
    }

    /**
     * 修改角色菜单
     *
     * @param dto SysRoleMenuDTO
     * @return AjaxResult
     */
    public Result<Object> changeMenu(SysRoleMenuDTO dto) {
        SysRole sysRole = this.getById(dto.getRoleId());
        if (BeanUtil.isEmpty(sysRole)) {
            return Result.error("角色不存在");
        }
        if (Constants.TENANT_ADMIN_ROLE_KEY.equals(sysRole.getRoleKey())) {
            return Result.error("管理员角色不允许修改");
        }
        //先删除角色菜单关联
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, dto.getRoleId()));
        //再新增角色菜单关联
        List<SysRoleMenu> list = dto.getMenuIds().stream().map(e -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(dto.getRoleId());
            sysRoleMenu.setMenuId(e);
            return sysRoleMenu;
        }).collect(Collectors.toList());
        boolean saveBatch = sysRoleMenuService.saveBatch(list);
        return saveBatch ? Result.success() : Result.error();
    }
}
