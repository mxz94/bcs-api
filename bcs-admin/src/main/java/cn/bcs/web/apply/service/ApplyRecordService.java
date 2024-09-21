package cn.bcs.web.apply.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bcs.common.constant.BalanceConstants;
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
import cn.bcs.web.callFeeRecord.service.CallFeeRecordService;
import cn.bcs.web.selectData.domain.SelectData;
import cn.bcs.web.selectData.service.SelectDataService;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CallFeeRecordService callFeeRecordService;

    /**
     * 合伙人或上级没有代理 本周不可体现佣金+350
     *
     * 代理商 + 200 上级 + 150  同时判断已办理多少 1 +
     * @param old
     * @param fromUser
     */
    private void calacFee(ApplyRecord old, SysUser fromUser) {
        Long dailiUserId = fromUser.getUserId();
        // 合伙人 或没有上级 直接加350
        if (SysUserType.HEHUO.getCode().equals(fromUser.getUserType()) || fromUser.getFromUserId() == null) {
            userService.addBalance(BalanceConstants.WAIT_IN_BALANCE, BalanceConstants.HEHUO_350,  dailiUserId);
            callFeeRecordService.saveCallFeeRecord(fromUser, WithdrawTypeEnum.YONGJIN, BalanceConstants.HEHUO_350, null, old.getId());
            if (fromUser.getFromUserId() != null) {
                SysUser parentUser = userService.getById(fromUser.getFromUserId());
                if (parentUser != null && parentUser.getNoApplyMonth() < BalanceConstants.noApplyMonth) {
                    userService.addBalance(BalanceConstants.WAIT_IN_BALANCE, BalanceConstants.HEHUO_35,  parentUser.getUserId());
                    callFeeRecordService.saveCallFeeRecord(parentUser, WithdrawTypeEnum.YONGJIN, BalanceConstants.HEHUO_35, null, old.getId());
                }
            }
        } else if (SysUserType.DAILI.getCode().equals(fromUser.getUserType())) {
        //    代理 + 200
            userService.addBalance(BalanceConstants.WAIT_IN_BALANCE, BalanceConstants.DAILI_200,  dailiUserId);
            callFeeRecordService.saveCallFeeRecord(fromUser, WithdrawTypeEnum.YONGJIN, BalanceConstants.DAILI_200, null, old.getId());
            Integer count = this.lambdaQuery().eq(ApplyRecord::getFromUserId, dailiUserId).eq(ApplyRecord::getStatus, ApplyStatus.APPROVED.getCode()).count();
                userService.lambdaUpdate().eq(SysUser::getUserId, dailiUserId)
                        .set(SysUser::getNoApplyMonth, 0)
                        .set(count >= 1, SysUser::getUserType, SysUserType.HEHUO.getCode()).update();

            // 上级 + 150
            SysUser parentUser = userService.getById(fromUser.getFromUserId());
            if (parentUser.getNoApplyMonth() < BalanceConstants.noApplyMonth) {
                userService.addBalance(BalanceConstants.WAIT_IN_BALANCE, BalanceConstants.DAILI_150,  parentUser.getUserId());
                callFeeRecordService.saveCallFeeRecord(parentUser, WithdrawTypeEnum.YONGJIN, BalanceConstants.DAILI_150, null, old.getId());
            }
        }
        //    修改办理人类型为代理人
        userService.lambdaUpdate().eq(SysUser::getUserId, old.getUserId()).set(SysUser::getUserType, SysUserType.DAILI.getCode()).update();
    }
    @Transactional(rollbackFor = Exception.class)
    public Result handleStatus(ApplyRecordHandleStatus dto) {
        ApplyRecord old = this.getById(dto.getId());
        if (! old.getTenantId().equals(SecurityUtils.getTenantId())) {
            return Result.error("非自己账号的数据");
        }
        if (ApplyStatus.PENDING.getCode().equals(old.getStatus()) && ApplyStatus.APPROVED.getCode().equals(dto.getStatus())) {
            SysUser user = userService.getById(old.getFromUserId());
            if (user == null) {
                return Result.error("推荐人不存在");
            }
            if (!SysUserType.DAILI.getCode().equals(user.getUserType()) && !SysUserType.HEHUO.getCode().equals(user.getUserType())) {
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
        if (!SysUserType.DAILI.getCode().equals(fromUser.getUserType()) && !SysUserType.HEHUO.getCode().equals(fromUser.getUserType())) {
            return Result.error("邀请人未审核通过，无法邀请人，请联系管理员");
        }
        if (!fromUser.getTenantId().equals(SecurityUtils.getTenantId())) {
            return Result.error("邀请人和申请人租户不一致");
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
        // 套餐结束时间
        DateTime endTime = DateUtil.offsetMonth(DateUtil.endOfMonth(new Date()), Integer.valueOf(taocan.getRemark()));
        applyRecord.setEndTime(endTime);
        applyRecord.setTenantId(SecurityUtils.getTenantId());
        this.save(applyRecord);

        userService.lambdaUpdate().set(SysUser::getNickName, dto.getName()).set(SysUser::getPhonenumber, dto.getPhone()).eq(SysUser::getUserId, SecurityUtils.getUserId()).update();
        return Result.success();
    }

    public List<MonthCallFeeVO> selectCallFee() {
        List<MonthCallFeeVO> callList = this.baseMapper.selectCallFee();
        return callList;
    }

    public Integer selectRecent3Count(Long userId) {
        Date now = new Date();
        DateTime startTime = DateUtil.offsetMonth(DateUtil.beginOfMonth(now), -3);
        Integer count = this.lambdaQuery().eq(ApplyRecord::getUserId, userId)
                .gt(ApplyRecord::getCreateTime, startTime)
                .lt(ApplyRecord::getCreateTime, now)
                .eq(ApplyRecord::getStatus, ApplyStatus.APPROVED.getCode()).count();
        return count;
    }

    public static void main(String[] args) {
        Date now = new Date();
        DateTime endTime =  DateUtil.beginOfMonth(now);
        DateTime startTime = DateUtil.offsetMonth(endTime, -3);
        System.out.println("startTime = " + startTime);
    }

    public List<ApplyRecord> selectMaxApplyRecord() {
        return this.baseMapper.selectMaxApplyRecord();
    }
}
