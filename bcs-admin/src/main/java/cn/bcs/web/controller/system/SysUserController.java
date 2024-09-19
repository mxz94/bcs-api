package cn.bcs.web.controller.system;

import cn.bcs.common.annotation.Log;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.Edit;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.common.utils.poi.ExcelUtil;
import cn.bcs.system.domain.TeamTreeVO;
import cn.bcs.system.domain.dto.SysUserDTO;
import cn.bcs.system.domain.dto.SysUserResetPwdDTO;
import cn.bcs.system.domain.dto.SysUserStatusDTO;
import cn.bcs.system.domain.query.SysUserQuery;
import cn.bcs.system.domain.vo.SysUserInfoVO;
import cn.bcs.system.domain.vo.SysUserVO;
import cn.bcs.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Resource
    private SysUserService userService;

    @ApiOperation("获取用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo<SysUserVO> list(SysUserQuery query) {
        startPage();
        return userService.selectUserList(query, getLoginUser());
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @ApiOperation("根据用户编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public Result<SysUserInfoVO> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        return userService.getInfo(userId);
    }

    @ApiOperation("新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Object> add(@Validated @RequestBody SysUserDTO dto) {
        LoginUser loginUser = getLoginUser();
        return userService.insertUser(dto, loginUser);
    }

    @ApiOperation("修改用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Object> edit(@Validated({Edit.class}) @RequestBody SysUserDTO dto) {
        return userService.updateUser(dto);
    }

    @ApiOperation("删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public Result<Object> remove(@PathVariable Long[] userIds) {
        LoginUser loginUser = getLoginUser();
        return userService.deleteUserByIds(userIds, loginUser);
    }

    @ApiOperation("重置密码")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public Result<Object> resetPwd(@RequestBody @Validated SysUserResetPwdDTO dto) {
        return userService.resetPwd(dto);
    }

    @ApiOperation("状态修改")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Result<Object> changeStatus(@RequestBody @Validated SysUserStatusDTO dto) {
        return userService.updateUserStatus(dto);
    }

    @ApiOperation("用户筛选框")
    @ApiImplicitParam(name = "nickName", value = "用户昵称", required = false, dataType = "String")
    @GetMapping("/select")
    public Result<Object> selectAll(String nickName) {
        return Result.success(userService.lambdaQuery().select(SysUser::getUserId, SysUser::getNickName).eq(SysUser::getDelFlag, 0).like(StringUtils.isNotEmpty(nickName), SysUser::getNickName, nickName).list());
    }


    @ApiOperation(value = "我的团队", tags = {"公众号"})
    @GetMapping("/teamTree")
    public Result<List<TeamTreeVO>> teamTree() {
        return userService.teamTree();
    }

    @ApiOperation(value = "所有用户")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/listAll")
    public Result<List<SysUser>> listAll() {
        return Result.success(userService.lambdaQuery().eq(SysUser::getDelFlag, 0).ne(SysUser::getUserType, SysUserType.ADMIN.getCode()).list());
    }
}
