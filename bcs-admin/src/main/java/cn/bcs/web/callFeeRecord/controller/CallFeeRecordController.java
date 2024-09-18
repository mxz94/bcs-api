package cn.bcs.web.callFeeRecord.controller;

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
import cn.bcs.web.callFeeRecord.domain.CallFeeRecord;
import cn.bcs.web.callFeeRecord.service.CallFeeRecordService;
import cn.bcs.common.core.page.TableDataInfo;

/**
 * 话费分成记录Controller
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@RestController
@RequestMapping("/callFeeRecord")
@Api("话费分成记录")
public class CallFeeRecordController extends BaseController {
    @Autowired
    private CallFeeRecordService callFeeRecordService;

/**
 * 查询话费分成记录列表
 */
@ApiOperation(value = "话费分成记录", tags = {"公众号"})
@PreAuthorize("@ss.hasPermi('callFeeRecord:list')")
@GetMapping("/list")
    public TableDataInfo list(CallFeeRecord callFeeRecord) {
        startPage();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (SysUserType.ADMIN.getCode() != user.getUserType()) {
            callFeeRecord.setUserId(user.getUserId());
        }
        List<CallFeeRecord> list = callFeeRecordService.list(new LambdaQueryWrapper<CallFeeRecord>(callFeeRecord));
        return getDataTable(list);
    }

    ///**
    // * 获取话费分成记录详细信息
    // */
    //@ApiOperation(value = "获取话费分成记录详细信息")
    //@PreAuthorize("@ss.hasPermi('callFeeRecord:query')")
    //@GetMapping(value = "/{id}")
    //public Result getInfo(@PathVariable("id") Long id) {
    //    return success(callFeeRecordService.getById(id));
    //}
    //
    ///**
    // * 新增话费分成记录
    // */
    //@ApiOperation(value = "新增话费分成记录")
    //@PreAuthorize("@ss.hasPermi('callFeeRecord:add')")
    //@Log(title = "话费分成记录", businessType = BusinessType.INSERT)
    //@PostMapping
    //public Result add(@RequestBody CallFeeRecord callFeeRecord) {
    //    return toAjax(callFeeRecordService.save(callFeeRecord));
    //}
    //
    ///**
    // * 修改话费分成记录
    // */
    //@ApiOperation(value = "修改话费分成记录")
    //@PreAuthorize("@ss.hasPermi('callFeeRecord:edit')")
    //@Log(title = "话费分成记录", businessType = BusinessType.UPDATE)
    //@PutMapping
    //public Result edit(@RequestBody CallFeeRecord callFeeRecord) {
    //    return toAjax(callFeeRecordService.updateById(callFeeRecord));
    //}
    //
    ///**
    // * 删除话费分成记录
    // */
    //@ApiOperation(value = "删除话费分成记录")
    //@PreAuthorize("@ss.hasPermi('callFeeRecord:remove')")
    //@Log(title = "话费分成记录", businessType = BusinessType.DELETE)
    //@DeleteMapping("/{ids}")
    //public Result remove(@PathVariable Long[] ids) {
    //    return toAjax(callFeeRecordService.removeByIds(Arrays.asList(ids)));
    //}
}
