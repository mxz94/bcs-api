package cn.bcs.web.selectData.controller;

import java.util.Arrays;
import java.util.List;

import cn.bcs.common.enums.SysCommonStatus;
import cn.bcs.web.selectData.constants.SelectDataType;
import cn.bcs.web.selectData.domain.vo.SelectDataQuery;
import cn.bcs.web.selectData.domain.vo.SelectDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.bcs.common.annotation.Log;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.web.selectData.domain.SelectData;
import cn.bcs.web.selectData.service.SelectDataService;
import cn.bcs.common.core.page.TableDataInfo;

/**
 * 选择内容Controller
 *
 * @author ruoyi
 * @date 2023-05-09
 */
@Api(tags = "选择内容")
@RestController
@RequestMapping("/system/selectData")
public class SelectDataController extends BaseController {
    @Autowired
    private SelectDataService selectDataService;

    /**
     * 查询选择内容列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public TableDataInfo list(SelectDataQuery query) {
        startPage();
        List<SelectData> list = selectDataService.selectList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "常见问题，问题选择框查询", tags = {"公众号"})
    @ApiImplicitParam(name = "type", value = SelectDataType.INFO, required = true)
    @GetMapping("/listAll2")
    public Result<List<SelectData>> listAll2(SelectDataQuery query) {
        query.setStatus(SysCommonStatus.NORMAL.getCode());
        List<SelectData> list = selectDataService.selectList(query);
        return Result.success(list);
    }

    /**
     * 查询选择内容列表
     */
    @ApiOperation(value = "常见问题， 套餐 选择框查询", tags = {"公众号"})
    @ApiImplicitParam(name = "type", value = SelectDataType.INFO, required = true)
    @GetMapping("/listAll")
    public Result<List<SelectDataVO>> selectList(SelectDataQuery query) {
        query.setStatus(SysCommonStatus.NORMAL.getCode());
        List<SelectDataVO> list = selectDataService.listAll(query);
        return Result.success(list);
    }

    /**
     * 获取选择内容详细信息
     */
    @ApiOperation(value = "获取选择内容详细信息")
    @GetMapping(value = "/info/{id}")
    @PreAuthorize("@ss.hasPermi('system:selectData:list')")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(selectDataService.getById(id));
    }

    /**
     * 新增选择内容
     */
    @ApiOperation(value = "新增选择内容")
    @Log(title = "选择内容", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('system:selectData:list')")
    public Result add(@RequestBody SelectData selectData) {
        selectData.setTenantId(getTenantId());
        return toAjax(selectDataService.save(selectData));
    }

    /**
     * 修改选择内容
     */
    @ApiOperation(value = "修改选择内容")
    @Log(title = "选择内容", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ss.hasPermi('system:selectData:list')")
    public Result edit(@RequestBody SelectData selectData) {
        return toAjax(selectDataService.updateById(selectData));
    }

    /**
     * 删除选择内容
     */
    @ApiOperation(value = "删除选择内容")
    @Log(title = "选择内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("@ss.hasPermi('system:selectData:list')")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(selectDataService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 批量设置状态
     */
    @ApiOperation(value = "批量设置状态")
    @Log(title = "选择内容", businessType = BusinessType.UPDATE)
    @PostMapping("/batchSetStatus/{ids}")
    @PreAuthorize("@ss.hasPermi('system:selectData:list')")
    public Result batchSetStatus(@PathVariable Long[] ids, Integer status) {
        return toAjax(selectDataService.lambdaUpdate().in(SelectData::getId, Arrays.asList(ids)).set(SelectData::getStatus, status).update());
    }
}
