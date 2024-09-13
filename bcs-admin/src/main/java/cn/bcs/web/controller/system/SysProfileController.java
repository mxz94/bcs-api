package cn.bcs.web.controller.system;

import cn.bcs.common.annotation.Log;
import cn.bcs.common.config.RuoYiConfig;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysRole;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.file.FileUploadUtils;
import cn.bcs.common.utils.file.MimeTypeUtils;
import cn.bcs.framework.web.service.TokenService;
import cn.bcs.system.domain.vo.SysUserVO;
import cn.bcs.system.service.SysRoleService;
import cn.bcs.system.service.SysUserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@Api(tags = "个人信息管理")
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Resource
    private SysUserService userService;
    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private TokenService tokenService;

    @ApiOperation(value = "获取个人信息", notes = "获取个人信息")
    @GetMapping
    public Result<SysUserVO> profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        SysUserVO vo = BeanUtil.copyProperties(user, SysUserVO.class);
        SysRole sysRole = sysRoleService.getById(user.getRoleId());
        vo.setRoleName(sysRole.getRoleName());
        return Result.success(vo);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        user.setUserName(sysUser.getUserName());
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        user.setAvatar(null);
        if (userService.updateById(user)) {
            // 更新缓存用户信息
            sysUser.setNickName(user.getNickName());
            sysUser.setPhonenumber(user.getPhonenumber());
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public Result updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新密码不能与旧密码相同");
        }
        boolean update = userService.update(new SysUser().setPassword(SecurityUtils.encryptPassword(newPassword)),
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserId, loginUser.getUser().getUserId()));
        if (update) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public Result<String> avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            boolean update = userService.update(new SysUser().setAvatar(avatar),
                    Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserId, loginUser.getUser().getUserId()));
            if (update) {
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return Result.success(avatar);
            }
        }
        return Result.error("上传图片异常，请联系管理员");
    }
}
