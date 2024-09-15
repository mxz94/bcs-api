package cn.bcs.web.apply.controller;

import java.util.Arrays;
import java.util.List;

import cn.bcs.web.apply.domain.dto.ApplyRecordDTO;
import cn.bcs.web.apply.domain.dto.ApplyRecordHandleStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import cn.bcs.web.apply.domain.ApplyRecord;
import cn.bcs.web.apply.service.ApplyRecordService;
import cn.bcs.common.core.page.TableDataInfo;

/**
 * 套餐申请记录Controller
 *
 * @author mxz
 * @date 2024-09-14
 */
@RestController
@RequestMapping("/apply")
@Api("套餐办理记录")
public class ApplyRecordController extends BaseController {
    @Autowired
    private ApplyRecordService applyRecordService;

/**
 * 查询套餐申请记录列表
 */
@ApiOperation(value = "查询套餐办理记录列表")
@GetMapping("/list")
    public TableDataInfo list(ApplyRecord applyRecord) {
        startPage();
        List<ApplyRecord> list = applyRecordService.list(new LambdaQueryWrapper<ApplyRecord>(applyRecord));
        return getDataTable(list);
    }

    /**
     * 获取套餐申请记录详细信息
     */
    @ApiOperation(value = "获取套餐申请记录详细信息")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(applyRecordService.getById(id));
    }

    /**
     * 新增套餐申请记录
     */
    @ApiOperation(value = "新增套餐申请记录")
    @Log(title = "套餐申请记录", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody ApplyRecord applyRecord) {
        return toAjax(applyRecordService.save(applyRecord));
    }

    /**
     * 修改套餐申请记录
     */
    @ApiOperation(value = "修改套餐申请记录")
    @Log(title = "套餐申请记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody ApplyRecord applyRecord) {
        return toAjax(applyRecordService.updateById(applyRecord));
    }

    /**
     * 删除套餐申请记录
     */
    @ApiOperation(value = "删除套餐申请记录")
    @Log(title = "套餐申请记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(applyRecordService.removeByIds(Arrays.asList(ids)));
    }

    @ApiOperation(value = "修改审核状态")
    @Log(title = "修改审核状态")
    @PostMapping("/handleStatus")
    public Result handleStatus(@RequestBody ApplyRecordHandleStatus applyRecordHandleStatus) {
        return applyRecordService.handleStatus(applyRecordHandleStatus);
    }

    /**
     * 新增套餐申请记录
     */
    @ApiOperation(value = "套餐申请", tags = {"公众号"})
    @Log(title = "套餐申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public Result apply(@Validated @RequestBody ApplyRecordDTO dto) {
        return applyRecordService.apply(dto);
    }

}
