package cn.bcs.web.yongjinRecord.controller;

import java.util.List;

import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.web.yongjinRecord.domain.YongjinRecord;
import cn.bcs.web.yongjinRecord.domain.dto.YongjinRecordQuery;
import cn.bcs.web.yongjinRecord.service.YongjinRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.page.TableDataInfo;

/**
 * 佣金分成记录Controller
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@RestController
@RequestMapping("/yongjinRecord")
@Api("佣金分成记录")
public class YongjinRecordController extends BaseController {
    @Autowired
    private YongjinRecordService yongjinRecordService;

/**
 * 查询佣金分成记录列表
 */
    @ApiOperation(value = "账单记录", tags = {"公众号"})
    @GetMapping("/list")
    public TableDataInfo<List<YongjinRecord>> list(YongjinRecordQuery query) {
        startPage();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (!SysUserType.ADMIN.getCode().equals(user.getUserType())) {
            query.setUserId(user.getUserId());
        }
        List<YongjinRecord> list = yongjinRecordService.lambdaQuery().eq(query.getUserId() != null, YongjinRecord::getUserId, query.getUserId()).list();
        return getDataTable(list);
    }

    ///**
    // * 获取佣金分成记录详细信息
    // */
    //@ApiOperation(value = "获取佣金分成记录详细信息")
    //@PreAuthorize("@ss.hasPermi('yongjinRecord:yongjinRecord:query')")
    //@GetMapping(value = "/{id}")
    //public Result getInfo(@PathVariable("id") Long id) {
    //    return success(yongjinRecordService.getById(id));
    //}
    //
    //
    ///**
    // * 修改佣金分成记录
    // */
    //@ApiOperation(value = "修改佣金分成记录")
    //@PreAuthorize("@ss.hasPermi('yongjinRecord:yongjinRecord:edit')")
    //@Log(title = "佣金分成记录", businessType = BusinessType.UPDATE)
    //@PutMapping
    //public Result edit(@RequestBody YongjinRecord yongjinRecord) {
    //    return toAjax(yongjinRecordService.updateById(yongjinRecord));
    //}

}
