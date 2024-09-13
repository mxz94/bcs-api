package cn.bcs.framework.web.service;

import cn.bcs.common.constant.UserConstants;
import cn.bcs.common.core.domain.entity.SysRole;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.system.service.SysMenuService;
import cn.bcs.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Component
public class SysPermissionService {
    @Resource
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (UserConstants.SUPER_ADMIN_USER_ID.equals(user.getUserId())) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.lambdaQuery().eq(SysRole::getRoleId,user.getRoleId()).list()
                            .stream().map(SysRole::getRoleKey).collect(Collectors.toList()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (UserConstants.SUPER_ADMIN_USER_ID.equals(user.getUserId())) {
            perms.add("*:*:*");
        } else {
            List<SysRole> roles = roleService.lambdaQuery().eq(SysRole::getRoleId,user.getRoleId()).list();
            if (!roles.isEmpty() && roles.size() > 1) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRole role : roles) {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
