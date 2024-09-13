package cn.bcs.web.tenant.service;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysRole;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.common.enums.SysCommonStatus;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.system.service.SysRoleService;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.tenant.domain.SysTenant;
import cn.bcs.web.tenant.domain.dto.TenantDTO;
import cn.bcs.web.tenant.domain.query.TenantQuery;
import cn.bcs.web.tenant.domain.vo.TenantVO;
import cn.bcs.web.tenant.mapper.SysTenantMapper;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 租户Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-08
 */
@Service
public class SysTenantService extends ServiceImpl<SysTenantMapper, SysTenant> {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 查询租户列表
     *
     * @return 租户集合
     */
    public TableDataInfo<TenantVO> selectTenantList(TenantQuery query) {
        List<TenantVO> list = baseMapper.selectTenantList(query);
        return TableDataInfo.ok(list);
    }

    /**
     * 获取租户详细信息
     *
     * @param tenantId 租户ID
     * @return 租户信息
     */
    public Result<TenantVO> getInfo(Long tenantId) {
        TenantVO vo = new TenantVO();
        if (StringUtils.isNotNull(tenantId)) {
            SysTenant tenant = this.getById(tenantId);
            SysUser sysUser = sysUserService.lambdaQuery()
                    .eq(SysUser::getTenantId, tenantId)
                    .eq(SysUser::getUserType, SysUserType.ADMIN.getCode()).one();
            BeanUtil.copyProperties(tenant, vo);
            vo.setUserName(sysUser.getUserName());
            vo.setRoleId(sysUser.getRoleId());
            vo.setUserId(sysUser.getUserId());
        }
        List<SysRole> list = sysRoleService.lambdaQuery().eq(SysRole::getTenantId, 0).list();
        vo.setRoles(list);
        return Result.success(vo);
    }

    /**
     * 新增租户
     *
     * @param dto 租户
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> addTenant(TenantDTO dto) {
        Integer count = sysUserService.lambdaQuery().eq(SysUser::getUserName, dto.getUserName()).count();
        if (count > 0) {
            return Result.error("用户名已存在");
        }
        SysTenant tenant = BeanUtil.copyProperties(dto, SysTenant.class);
        tenant.setStatus(SysCommonStatus.NORMAL.getCode());
        tenant.setDelFlag(SysCommonStatus.NORMAL.getCode());
        this.save(tenant);
        SysUser sysUser = new SysUser()
                .setRoleId(dto.getRoleId())
                .setUserName(dto.getUserName())
                .setNickName(dto.getUserName())
                .setTenantId(tenant.getTenantId())
                .setUserType(SysUserType.ADMIN.getCode())
                .setPassword(SecurityUtils.encryptPassword(dto.getPassword()))
                .setStatus(SysCommonStatus.NORMAL.getCode())
                .setDelFlag(SysCommonStatus.NORMAL.getCode());
        sysUserService.save(sysUser);
        //TODO 基础默认数据
        return Result.success();
    }

    /**
     * 修改租户
     *
     * @param dto 租户
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> editTenant(TenantDTO dto) {
        Integer count = sysUserService.lambdaQuery()
                .eq(SysUser::getUserName, dto.getUserName())
                .ne(SysUser::getUserId, dto.getUserId()).count();
        if (count > 0) {
            return Result.error("用户名已存在");
        }
        SysTenant tenant = BeanUtil.copyProperties(dto, SysTenant.class);
        this.updateById(tenant);
        SysUser sysUser = new SysUser().setUserId(dto.getUserId()).setUserName(dto.getUserName());
        boolean update = sysUserService.updateById(sysUser);
        return update ? Result.success() : Result.error();
    }

    /**
     * 更新租户状态
     *
     * @param dto 租户
     * @return 结果
     */
    public Result<Object> updateTenantStatus(SysTenant dto) {
        boolean update = this.update(new SysTenant().setStatus(dto.getStatus()),
                Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getTenantId, dto.getTenantId()));
        return update ? Result.success() : Result.error();
    }
}
