package cn.bcs.web.withdrawRecord.controller;

import java.util.List;

import cn.bcs.common.annotation.RepeatSubmit;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.web.withdrawRecord.constants.WithdrawStatusEnum;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.bcs.web.withdrawRecord.domain.dto.TixianDTO;
import cn.bcs.web.withdrawRecord.domain.dto.TixianStatusDTO;
import cn.bcs.web.withdrawRecord.domain.query.WithDrawRecordQuery;
import cn.bcs.web.withdrawRecord.domain.vo.WithdrawRecordVO;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.bcs.common.annotation.Log;
import io.swagger.annotations.ApiOperation;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.web.withdrawRecord.service.WithdrawRecordService;
import cn.bcs.common.core.page.TableDataInfo;

/**
 * 提现记录Controller
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@RestController
@RequestMapping("/withdrawRecord")
@Api("提现记录")
public class WithdrawRecordController extends BaseController {
    @Autowired
    private WithdrawRecordService withdrawRecordService;

    /**
     * 查询提现记录列表
     */
    @ApiOperation(value = "现金提现记录", tags = {"公众号"})
    @GetMapping("/list")
    public TableDataInfo< List<WithdrawRecordVO>> list(WithDrawRecordQuery query) {
        startPage();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (!SysUserType.ADMIN.getCode().equals(user.getUserType())) {
            query.setUserId(user.getUserId());
        }
        if (! SecurityUtils.isAdmin(user.getUserId())) {
            query.setTenantId(user.getTenantId());
        }
        List<WithdrawRecordVO> list = withdrawRecordService.selectList(query);
        return getDataTable(list);
    }

    /**
     * 获取提现记录详细信息
     */
    @ApiOperation(value = "获取提现记录详细信息")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(withdrawRecordService.getById(id));
    }

    ///**
    // * 新增提现记录
    // */
    //@ApiOperation(value = "新增提现记录")
    //@PreAuthorize("@ss.hasPermi('withdrawRecord:withdrawRecord:add')")
    //@Log(title = "提现记录", businessType = BusinessType.INSERT)
    //@PostMapping
    //public Result add(@RequestBody WithdrawRecord withdrawRecord) {
    //    return toAjax(withdrawRecordService.save(withdrawRecord));
    //}
    //
    ///**
    // * 修改提现记录
    // */
    //@ApiOperation(value = "修改提现记录")
    //@PreAuthorize("@ss.hasPermi('withdrawRecord:withdrawRecord:edit')")
    //@Log(title = "提现记录", businessType = BusinessType.UPDATE)
    //@PutMapping
    //public Result edit(@RequestBody WithdrawRecord withdrawRecord) {
    //    return toAjax(withdrawRecordService.updateById(withdrawRecord));
    //}
    //
    ///**
    // * 删除提现记录
    // */
    //@ApiOperation(value = "删除提现记录")
    //@PreAuthorize("@ss.hasPermi('withdrawRecord:withdrawRecord:remove')")
    //@Log(title = "提现记录", businessType = BusinessType.DELETE)
    //@DeleteMapping("/{ids}")
    //public Result remove(@PathVariable Long[] ids) {
    //    return toAjax(withdrawRecordService.removeByIds(Arrays.asList(ids)));
    //}

    @ApiOperation(value = "处理提现单")
    @PreAuthorize("@ss.hasPermi('withdrawRecord:withdrawRecord:edit')")
    @Log(title = "处理提现单", businessType = BusinessType.UPDATE)
    @PostMapping("/handleStatus")
    public Result handleStatus(@RequestBody TixianStatusDTO dto) {
        WithdrawStatusEnum byCode = WithdrawStatusEnum.getByCode(dto.getStatus());
        if (byCode == null) {
            return Result.error("状态不正确");
        }
        return withdrawRecordService.handleStatus(dto);
    }

    @ApiOperation(value = "提现", notes = "代理提现")
    @PostMapping("/tixian")
    @RepeatSubmit
    public Result tixian(@RequestBody TixianDTO dto) {
        WithdrawTypeEnum byCode = WithdrawTypeEnum.getByCode(dto.getType());
        if (BeanUtil.isEmpty(byCode)) {
            return Result.error("提现类型错误");
        }
        return withdrawRecordService.tixian(byCode, dto.getAmount());
    }
}
