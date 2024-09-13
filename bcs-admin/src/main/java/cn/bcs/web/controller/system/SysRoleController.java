package cn.bcs.web.controller.system;

import cn.bcs.common.annotation.Log;
import cn.bcs.common.constant.HttpStatus;
import cn.bcs.common.constant.UserConstants;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysRole;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.framework.web.service.SysPermissionService;
import cn.bcs.framework.web.service.TokenService;
import cn.bcs.system.domain.dto.SysRoleMenuDTO;
import cn.bcs.system.domain.vo.SysRoleTreeVO;
import cn.bcs.system.service.SysRoleService;
import cn.bcs.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Resource
    private SysRoleService roleService;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysPermissionService permissionService;

    @Resource
    private SysUserService userService;

    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo<SysRole> list(SysRole role) {
        startPage();
        return roleService.selectRoleList(role, getLoginUser());
    }

    @ApiOperation("根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        return Result.success(roleService.getById(roleId));
    }

    @ApiOperation("新增角色")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "新增角色", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Object> add(@Validated @RequestBody SysRole role) {
        return roleService.insertRole(role, getLoginUser());
    }

    @ApiOperation("修改角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "修改角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Object> edit(@Validated @RequestBody SysRole role) {
        Result<Object> result = roleService.updateRole(role);
        if (HttpStatus.SUCCESS == result.getCode()) {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (UserConstants.SUPER_ADMIN_USER_ID.equals(loginUser.getUserId())) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
        }
        return result;
    }

    @ApiOperation("修改角色状态")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "修改角色状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Result<Object> changeStatus(@RequestBody SysRole role) {
        return roleService.updateRoleStatus(role);
    }

    @ApiOperation("删除角色")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "删除角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public Result<Object> remove(@PathVariable Long[] roleIds) {
        return roleService.deleteRoleByIds(roleIds);
    }

    @ApiOperation("获取角色选择框列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    @SuppressWarnings("unchecked")
    public Result<List<SysRole>> optionselect() {
        return Result.success(roleService.lambdaQuery().orderByAsc(SysRole::getRoleId).list());
    }

    @ApiOperation("获取对应角色权限树列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/menuTree/{roleId}")
    public Result<SysRoleTreeVO> menuTree(@PathVariable("roleId") Long roleId) {
        return roleService.menuTree(roleId, getLoginUser());
    }

    @ApiOperation("修改角色权限")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "修改角色权限", businessType = BusinessType.UPDATE)
    @PutMapping("/changeMenu")
    public Result<Object> changeMenu(@RequestBody @Validated SysRoleMenuDTO dto) {
        return roleService.changeMenu(dto);
    }
}
