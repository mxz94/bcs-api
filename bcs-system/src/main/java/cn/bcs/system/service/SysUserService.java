package cn.bcs.system.service;

import cn.bcs.common.constant.UserConstants;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysRole;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.domain.model.LoginUser;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.common.enums.SysCommonStatus;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.exception.ServiceException;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.common.utils.bean.BeanValidators;
import cn.bcs.system.domain.TeamTreeVO;
import cn.bcs.system.domain.dto.SysUserDTO;
import cn.bcs.system.domain.dto.SysUserResetPwdDTO;
import cn.bcs.system.domain.dto.SysUserStatusDTO;
import cn.bcs.system.domain.query.SysUserQuery;
import cn.bcs.system.domain.vo.SysUserInfoVO;
import cn.bcs.system.domain.vo.SysUserVO;
import cn.bcs.system.mapper.SysRoleMapper;
import cn.bcs.system.mapper.SysUserMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    @Resource
    protected Validator validator;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysConfigService configService;

    /**
     * 根据条件分页查询用户列表
     *
     * @param query 用户信息
     * @return 用户信息集合信息
     */
    public TableDataInfo<SysUserVO> selectUserList(SysUserQuery query, LoginUser loginUser) {
        List<SysUserVO> sysUsers = baseMapper.selectUserList(query, loginUser.getTenantId());
        return TableDataInfo.ok(sysUsers);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public Result<SysUserInfoVO> getInfo(Long userId) {
        SysUserInfoVO vo = new SysUserInfoVO();
        List<SysRole> roles = roleMapper.selectList(null);
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = this.getById(userId);
            BeanUtil.copyProperties(sysUser, vo);
        }
        vo.setRoles(roles);
        return Result.success(vo);
    }

    /**
     * 新增保存用户信息
     *
     * @param dto       用户信息
     * @param loginUser 登录用户
     * @return 结果
     */
    public Result<Object> insertUser(SysUserDTO dto, LoginUser loginUser) {
        Integer count = baseMapper.checkUserNameUnique(dto.getUserName(), null);
        if (count > 0) {
            return Result.error("新增用户'" + dto.getUserName() + "'失败，登录账号已存在");
        }
        SysUser sysUser = BeanUtil.copyProperties(dto, SysUser.class)
                .setTenantId(loginUser.getTenantId())
                .setPassword(SecurityUtils.encryptPassword(dto.getPassword()))
                .setNickName(StrUtil.isBlank(dto.getNickName()) ? dto.getUserName() : dto.getNickName())
                .setStatus(SysCommonStatus.NORMAL.getCode())
                .setDelFlag(SysCommonStatus.NORMAL.getCode());
        boolean save = this.save(sysUser);
        return save ? Result.success() : Result.error();
    }

    /**
     * 修改保存用户信息
     *
     * @param dto 用户信息
     * @return 结果
     */
    public Result<Object> updateUser(SysUserDTO dto) {
        SysUser sysUser = BeanUtil.copyProperties(dto, SysUser.class);
        if (UserConstants.SUPER_ADMIN_USER_ID.equals(sysUser.getUserId())) {
            return Result.error("不允许修改超级管理员用户");
        }
        Integer count = baseMapper.checkUserNameUnique(dto.getUserName(), dto.getUserId());
        if (count > 0) {
            return Result.error("修改用户'" + dto.getUserName() + "'失败，登录账号已存在");
        }
        sysUser.setPassword(null);
        boolean update = this.updateById(sysUser);
        return update ? Result.success() : Result.error();
    }

    /**
     * 修改用户状态
     *
     * @param dto 用户信息
     * @return 结果
     */
    public Result<Object> updateUserStatus(SysUserStatusDTO dto) {
        if (CollUtil.contains(dto.getUserIds(), 1L)) {
            return Result.error("不允许操作超级管理员用户");
        }
        boolean update = this.update(new SysUser().setStatus(dto.getStatus()),
                Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, dto.getUserIds()));
        return update ? Result.success() : Result.error();
    }

    /**
     * 重置用户密码
     *
     * @param dto 用户信息
     * @return 结果
     */
    public Result<Object> resetPwd(SysUserResetPwdDTO dto) {
        if (CollUtil.contains(dto.getUserIds(), 1L)) {
            return Result.error("不允许操作超级管理员用户");
        }
        boolean update = this.update(new SysUser().setPassword(SecurityUtils.encryptPassword(dto.getPassword())),
                Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, dto.getUserIds()));
        return update ? Result.success() : Result.error();
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public Result<Object> deleteUserByIds(Long[] userIds, LoginUser loginUser) {
        if (ArrayUtils.contains(userIds, loginUser.getUserId())) {
            return Result.error("当前用户不能删除");
        }
        if (ArrayUtils.contains(userIds, 1L)) {
            return Result.error("不允许操作超级管理员用户");
        }
        boolean remove = this.removeByIds(Arrays.asList(userIds));
        return remove ? Result.success() : Result.error();
    }

    /**
     * 更新登录信息
     *
     * @param userId    用户ID
     * @param loginIp   登录IP地址
     * @param loginDate 登录时间
     */
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        userMapper.updateLoginInfo(userId, loginIp, loginDate);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */

    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.save(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    if (UserConstants.SUPER_ADMIN_USER_ID.equals(user.getUserId())) {
                        throw new ServiceException("不允许修改超级管理员用户");
                    }
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    this.updateById(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    public void addWaitBalance2Balance() {
        this.baseMapper.addWaitBalance2Balance();
    }

    public Result<List<TeamTreeVO>> teamTree() {
        Long currentUserId = SecurityUtils.getUserId();

        // 如果是管理员，查找所有下级用户的树状结构
        if (SysUserType.ADMIN.getCode().equals(SecurityUtils.getLoginUser().getUser().getUserType())) {
            List<SysUser> topLevelUsers = this.lambdaQuery()
                    .eq(SysUser::getDelFlag, "0")
                    .eq(SysUser::getTenantId, SecurityUtils.getTenantId())
                    .ne(SysUser::getUserType, SysUserType.ADMIN.getCode())
                    .isNull(SysUser::getFromUserId)
                    .list();

            // 构建完整的用户树
            List<TeamTreeVO> userTree = buildUserTree(topLevelUsers, Integer.MAX_VALUE); // 管理员无限层
            return Result.success(userTree);
        } else {
            // 普通用户只查找两层下级用户
            List<SysUser> secondLevelUsers = this.lambdaQuery()
                    .eq(SysUser::getDelFlag, "0")
                    .eq(SysUser::getUserId, currentUserId)
                    .list();

            // 构建两层用户树
            List<TeamTreeVO> userTree = buildUserTree(secondLevelUsers, 2); // 限制高度为2层
            return Result.success(userTree);
        }
    }

    // 查找用户并构建树状结构，限制层级深度
    private List<TeamTreeVO> buildUserTree(List<SysUser> users, int level) {
        if (level == 0 || users == null || users.isEmpty()) {
            return Collections.emptyList(); // 如果层级为0或无用户，不继续查找
        }

        List<TeamTreeVO> tree = new ArrayList<>();
        for (SysUser user : users) {
            TeamTreeVO vo = new TeamTreeVO();
            BeanUtil.copyProperties(user, vo);

            // 如果还有下一级，且当前层级限制未达到，继续查找子用户
            if (level >= 1) {
                List<SysUser> subUsers = this.lambdaQuery()
                        .eq(SysUser::getDelFlag, "0")
                        .eq(SysUser::getFromUserId, user.getUserId())
                        .list();

                vo.setChildren(buildUserTree(subUsers, level - 1)); // 递归构建子树
            }

            tree.add(vo);
        }
        return tree;
    }

    public void addBalance(String waitInBalance, BigDecimal amount, Long userId) {
        this.baseMapper.addBalance(waitInBalance, amount, userId);
    }
}
