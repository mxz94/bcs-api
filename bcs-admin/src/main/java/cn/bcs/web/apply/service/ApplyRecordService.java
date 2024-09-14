package cn.bcs.web.apply.service;

import java.math.BigDecimal;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.constants.ApplyStatus;
import cn.bcs.web.apply.domain.ApplyRecord;
import cn.bcs.web.apply.domain.dto.ApplyRecordDTO;
import cn.bcs.web.apply.domain.dto.ApplyRecordHandleStatus;
import cn.bcs.web.apply.mapper.ApplyRecordMapper;
import cn.bcs.web.selectData.domain.SelectData;
import cn.bcs.web.selectData.service.SelectDataService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 套餐申请记录Service业务层处理
 *
 * @author mxz
 * @date 2024-09-14
 */
@Service
public class  ApplyRecordService extends ServiceImpl<ApplyRecordMapper, ApplyRecord> {
    @Resource
    private SysUserService userService;
    @Resource
    private SelectDataService selectDataService;

    private void calacFee(ApplyRecord old, SysUser user) {
        // TODO: 2024/9/14 修改用户为代理用户， 并且 加佣金
    }
    public Result handleStatus(ApplyRecordHandleStatus dto) {
        ApplyRecord old = this.getById(dto.getId());
        if (ApplyStatus.PENDING.getCode().equals(old.getStatus()) && ApplyStatus.APPROVED.getCode().equals(dto.getStatus())) {
            SysUser user = userService.getById(old.getFromUserId());
            if (user == null) {
                return Result.error("推荐人不存在");
            }
            calacFee(old, user);
        }
        this.lambdaUpdate().eq(ApplyRecord::getId, dto.getId())
                .set(ApplyRecord::getStatus, dto.getStatus())
                .set(ApplyRecord::getRemark, dto.getRemark()).update();
        return Result.success();
    }

    public Result apply(ApplyRecordDTO dto) {
        SysUser fromUser = userService.getById(dto.getFromUserId());
        // TODO: 2024/9/14 验证是否为代理用户

        SelectData taocan = selectDataService.getById(dto.getTaocanId());
        ApplyRecord applyRecord = BeanUtil.copyProperties(dto, ApplyRecord.class);
        applyRecord.setTaocanName(taocan.getName());
        if (taocan.getValue() != null) {
            applyRecord.setTaocanValue(BigDecimal.valueOf(Long.valueOf(taocan.getValue())));
        }
        applyRecord.setStatus(ApplyStatus.PENDING.getCode());
        this.save(applyRecord);

        return Result.success();
    }
}
