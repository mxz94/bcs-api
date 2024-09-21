package cn.bcs.web.callFeeRecord.controller;

import java.util.Arrays;
import java.util.List;

import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.web.callFeeRecord.domain.query.RecordQuery;
import cn.bcs.web.callFeeRecord.domain.vo.CallFeeRecordVO;
import io.swagger.annotations.Api;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@GetMapping("/list")
    public TableDataInfo list(@Validated RecordQuery query) {
        startPage();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (!SysUserType.ADMIN.getCode().equals(user.getUserType())) {
            query.setUserId(user.getUserId());
        }
        if (! SecurityUtils.isAdmin(user.getUserId())) {
            query.setTenantId(user.getTenantId());
        }
        List<CallFeeRecordVO> list = callFeeRecordService.selectRecordListByType(query);
        return getDataTable(list);
    }
}
