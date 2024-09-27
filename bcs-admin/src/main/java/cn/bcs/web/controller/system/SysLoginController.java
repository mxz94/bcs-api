package cn.bcs.web.controller.system;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysMenu;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.LoginBody;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.framework.web.service.SysLoginService;
import cn.bcs.framework.web.service.SysPermissionService;
import cn.bcs.system.domain.vo.LoginVO;
import cn.bcs.system.domain.vo.RouterVo;
import cn.bcs.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Api(tags = "登录验证")
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword());
        return Result.success(new LoginVO().setToken(token));
    }
    @PostMapping("/logRegister")
    public  Result<LoginVO> logRegister(@RequestBody LoginBody loginBody) {
        return loginService.logRegister(loginBody.getUsername(), loginBody.getPassword(), loginBody.getTenantId());
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("getInfo")
    public Result<LoginVO> getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        return Result.success(new LoginVO()
                .setUser(user)
                .setRoles(roles)
                .setPermissions(permissions));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation("获取路由信息")
    @GetMapping("getRouters")
    public Result<List<RouterVo>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return Result.success(menuService.buildMenus(menus));
    }
}
