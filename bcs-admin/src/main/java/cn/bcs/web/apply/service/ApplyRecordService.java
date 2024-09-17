package cn.bcs.web.apply.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.constants.ApplyStatus;
import cn.bcs.web.apply.domain.ApplyRecord;
import cn.bcs.web.apply.domain.dto.ApplyRecordDTO;
import cn.bcs.web.apply.domain.dto.ApplyRecordHandleStatus;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import cn.bcs.web.apply.mapper.ApplyRecordMapper;
import cn.bcs.web.selectData.domain.SelectData;
import cn.bcs.web.selectData.service.SelectDataService;
import cn.bcs.web.yongjinRecord.domain.YongjinRecord;
import cn.bcs.web.yongjinRecord.service.YongjinRecordService;
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
    @Resource
    private YongjinRecordService yongjinRecordService;

    /**
     * 合伙人或上级没有代理 本周不可体现佣金+350
     *
     * 代理商 + 200 上级 + 150  同时判断已办理多少 1 +
     * @param old
     * @param fromUser
     */
    private void calacFee(ApplyRecord old, SysUser fromUser) {
        SysUser banLiUser = userService.getById(old.getUserId());
        // 合伙人 或没有上级 直接加350
        if (SysUserType.HEHUO.getCode().equals(fromUser.getUserType()) || fromUser.getFromUserId() == null) {
            fromUser.setBalance(fromUser.getWaitInBalance().add(BigDecimal.valueOf(350)));
            userService.lambdaUpdate().set(SysUser::getWaitInBalance, fromUser.getWaitInBalance()).eq(SysUser::getUserId, old.getUserId()).update();
            yongjinRecordService.addRecord(fromUser, BigDecimal.valueOf(350), old.getId());
        } else if (SysUserType.DAILI.getCode().equals(fromUser.getUserType())) {
        //    代理 + 200 上级 + 150
            fromUser.setBalance(fromUser.getWaitInBalance().add(BigDecimal.valueOf(200)));
            userService.lambdaUpdate().set(SysUser::getWaitInBalance, fromUser.getWaitInBalance()).eq(SysUser::getUserId, old.getUserId()).update();
            yongjinRecordService.addRecord(fromUser, BigDecimal.valueOf(200), old.getId());

            SysUser parentUser = userService.getById(fromUser.getFromUserId());
            userService.lambdaUpdate().set(SysUser::getWaitInBalance, parentUser.getWaitInBalance().add(BigDecimal.valueOf(150))).eq(SysUser::getUserId, parentUser.getUserId()).update();
            yongjinRecordService.addRecord(parentUser, BigDecimal.valueOf(150), old.getId());

            Integer count = this.lambdaQuery().eq(ApplyRecord::getFromUserId, fromUser.getUserId()).eq(ApplyRecord::getStatus, ApplyStatus.APPROVED.getCode()).count();
            if (count >= 1) {
                userService.lambdaUpdate().eq(SysUser::getUserId, fromUser.getUserId()).set(SysUser::getUserType, SysUserType.HEHUO.getCode()).update();
            }
        }
        //    修改办理人类型为代理人
        userService.lambdaUpdate().eq(SysUser::getUserId, old.getUserId()).set(SysUser::getUserType, SysUserType.DAILI.getCode()).update();
    }
    public Result handleStatus(ApplyRecordHandleStatus dto) {
        ApplyRecord old = this.getById(dto.getId());
        if (ApplyStatus.PENDING.getCode().equals(old.getStatus()) && ApplyStatus.APPROVED.getCode().equals(dto.getStatus())) {
            SysUser user = userService.getById(old.getFromUserId());
            if (user == null) {
                return Result.error("推荐人不存在");
            }
            if (user.getUserType() != SysUserType.DAILI.getCode() && user.getUserType() != SysUserType.HEHUO.getCode()) {
                return Result.error("推荐人必须是代理用户");
            }
            calacFee(old, user);
        }
        this.lambdaUpdate().eq(ApplyRecord::getId, dto.getId())
                .set(ApplyRecord::getStatus, dto.getStatus())
                .set(ApplyRecord::getRemark, dto.getRemark()).update();
        return Result.success();
    }

    public Result apply(ApplyRecordDTO dto) /**/{
        SysUser fromUser = userService.getById(dto.getFromUserId());
        if (fromUser.getUserType() != SysUserType.DAILI.getCode() && fromUser.getUserType() != SysUserType.HEHUO.getCode()) {
            return Result.error("推荐人必须是代理用户");
        }
        Integer count = this.lambdaQuery().eq(ApplyRecord::getUserId, SecurityUtils.getUserId()).in(ApplyRecord::getStatus, Arrays.asList(ApplyStatus.PENDING.getCode(), ApplyStatus.APPROVED.getCode())).count();
        if (count > 0) {
            return Result.error("当前微信有正在办理中或已办理过的单子");
        }

        SelectData taocan = selectDataService.getById(dto.getTaocanId());
        ApplyRecord applyRecord = BeanUtil.copyProperties(dto, ApplyRecord.class);
        applyRecord.setUserId(SecurityUtils.getUserId());
        applyRecord.setStatus(ApplyStatus.PENDING.getCode());
        BeanUtil.copyProperties(taocan, applyRecord);
        this.save(applyRecord);

        userService.lambdaUpdate().set(SysUser::getNickName, dto.getName()).eq(SysUser::getUserId, SecurityUtils.getUserId()).update();
        return Result.success();
    }

    public List<MonthCallFeeVO> selectCallFee() {
        List<MonthCallFeeVO> callList = this.baseMapper.selectCallFee();
        return callList;
    }
}
