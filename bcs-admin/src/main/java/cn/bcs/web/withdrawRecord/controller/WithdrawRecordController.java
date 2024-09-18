package cn.bcs.web.withdrawRecord.controller;

import java.util.Arrays;
import java.util.List;

import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.bcs.common.annotation.Log;
import io.swagger.annotations.ApiOperation;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.web.withdrawRecord.domain.WithdrawRecord;
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
@PreAuthorize("@ss.hasPermi('withdrawRecord:withdrawRecord:list')")
@GetMapping("/list")
    public TableDataInfo list(WithdrawRecord withdrawRecord) {
        startPage();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (SysUserType.ADMIN.getCode() != user.getUserType()) {
            withdrawRecord.setCreateBy(user.getUserId().toString());
        }
        List<WithdrawRecord> list = withdrawRecordService.list(new LambdaQueryWrapper<WithdrawRecord>(withdrawRecord));
        return getDataTable(list);
    }

    ///**
    // * 获取提现记录详细信息
    // */
    //@ApiOperation(value = "获取提现记录详细信息")
    //@PreAuthorize("@ss.hasPermi('withdrawRecord:withdrawRecord:query')")
    //@GetMapping(value = "/{id}")
    //public Result getInfo(@PathVariable("id") Long id) {
    //    return success(withdrawRecordService.getById(id));
    //}
    //
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
}
