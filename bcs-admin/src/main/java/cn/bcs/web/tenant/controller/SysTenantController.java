package cn.bcs.web.tenant.controller;

import cn.bcs.common.annotation.Log;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.model.Edit;
import cn.bcs.common.core.page.TableDataInfo;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.web.tenant.domain.SysTenant;
import cn.bcs.web.tenant.domain.dto.TenantDTO;
import cn.bcs.web.tenant.domain.query.TenantQuery;
import cn.bcs.web.tenant.domain.vo.TenantVO;
import cn.bcs.web.tenant.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 租户Controller
 *
 * @author ruoyi
 * @date 2023-05-08
 */
@Api(tags = "租户管理")
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController {
    @Resource
    private SysTenantService sysTenantService;

    @ApiOperation(value = "查询租户列表")
    @PreAuthorize("@ss.hasPermi('system:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo<TenantVO> list(TenantQuery query) {
        startPage();
        return sysTenantService.selectTenantList(query);
    }

    @ApiOperation(value = "获取租户详细信息")
    @PreAuthorize("@ss.hasPermi('system:tenant:query')")
    @GetMapping(value = {"/", "/{tenantId}"})
    public Result<TenantVO> getInfo(@PathVariable(value = "tenantId", required = false) Long tenantId) {
        return sysTenantService.getInfo(tenantId);
    }

    @ApiOperation(value = "新增租户")
    @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "新增租户", businessType = BusinessType.INSERT)
    @PostMapping()
    public Result<Object> add(@RequestBody @Validated TenantDTO dto) {
        return sysTenantService.addTenant(dto);
    }

    @ApiOperation(value = "修改租户")
    @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "修改租户", businessType = BusinessType.UPDATE)
    @PutMapping()
    public Result<Object> edit(@RequestBody @Validated({Edit.class}) TenantDTO dto) {
        return sysTenantService.editTenant(dto);
    }

    @ApiOperation(value = "删除租户")
    @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "删除租户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tenantIds}")
    public Result<Object> remove(@PathVariable Long[] tenantIds) {
        boolean update = sysTenantService.removeByIds(Arrays.asList(tenantIds));
        return update ? Result.success() : Result.error();
    }

    @ApiOperation(value = "启用禁用租户")
    @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "启用禁用租户", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Result<Object> changeStatus(@RequestBody @Validated SysTenant dto) {
        return sysTenantService.updateTenantStatus(dto);
    }
}
