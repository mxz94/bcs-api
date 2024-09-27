package cn.bcs.web.withdrawRecord.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import cn.bcs.common.constant.BalanceConstants;
import cn.bcs.common.constant.Constants;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.callFeeRecord.service.CallFeeRecordService;
import cn.bcs.web.third.domain.vo.WechatJsSdkSignVO;
import cn.bcs.web.withdrawRecord.constants.WithdrawStatusEnum;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.bcs.web.withdrawRecord.domain.dto.TixianStatusDTO;
import cn.bcs.web.withdrawRecord.domain.query.WithDrawRecordQuery;
import cn.bcs.web.withdrawRecord.domain.vo.WithdrawRecordVO;
import org.springframework.stereotype.Service;
import cn.bcs.web.withdrawRecord.domain.WithdrawRecord;
import cn.bcs.web.withdrawRecord.mapper.WithdrawRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * 提现记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Service
public class  WithdrawRecordService extends ServiceImpl<WithdrawRecordMapper, WithdrawRecord> {
    @Resource
    CallFeeRecordService callFeeRecordService;
    @Resource
    SysUserService sysUserService;
    public Result tixian(WithdrawTypeEnum type, BigDecimal amount) {
        SysUser user = sysUserService.getById(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(user.getShoukuanUrl())) {
            return Result.error("收款码未上传");
        }
        if (type.equals(WithdrawTypeEnum.YONGJIN)) {
            if (BigDecimalUtils.isLessThan(user.getBalance(), amount)) {
                return Result.error("佣金不足");
            }
            //if (checkWithdrawal(type)) {
            //    return Result.error("周日才可提现佣金");
            //}
            if (BigDecimalUtils.isLessThan(amount, BalanceConstants.MIN_YONGJIN)) {
                return Result.error("提现金额不能小于" + BalanceConstants.MIN_YONGJIN);
            }
            WithdrawRecord withdrawRecord = new WithdrawRecord();
            withdrawRecord.setOldBalance(user.getBalance());
            withdrawRecord.setNewBalance(BigDecimalUtils.add(user.getBalance(), amount.negate()));
            withdrawRecord.setAmount(amount);
            withdrawRecord.setRealAmount(BigDecimalUtils.subtract(amount, BigDecimalUtils.multiplyPercentage(amount, BalanceConstants.SUODESHUI)));
            withdrawRecord.setRate(BalanceConstants.SUODESHUI);
            withdrawRecord.setType(type.getCode());
            withdrawRecord.setUserId(user.getUserId());
            withdrawRecord.setStatus("0");
            withdrawRecord.setTenantId(user.getTenantId());
            this.save(withdrawRecord);
            callFeeRecordService.saveCallFeeRecord(user, WithdrawTypeEnum.YONGJIN, amount.negate(), "提现", withdrawRecord.getId());
            sysUserService.addBalance(BalanceConstants.BALANCE, amount.negate(), user.getUserId());

        } else if (type.equals(WithdrawTypeEnum.HUAFEIFENCHENG)) {
            if (BigDecimalUtils.isLessThan(user.getCallBalance(), amount)) {
                return Result.error("话费分成不足");
            }
            //if (checkWithdrawal(type)) {
            //    return Result.error("每月28日提现话费分成");
            //}
            if (BigDecimalUtils.isLessThan(amount, BalanceConstants.MIN_HUAFEI)) {
                return Result.error("提现金额不能小于" + BalanceConstants.MIN_HUAFEI);
            }
            WithdrawRecord withdrawRecord = new WithdrawRecord();
            withdrawRecord.setOldBalance(user.getCallBalance());
            withdrawRecord.setNewBalance(BigDecimalUtils.add(user.getBalance(), amount.negate()));
            withdrawRecord.setAmount(amount);
            withdrawRecord.setRealAmount(BigDecimalUtils.subtract(amount, BigDecimalUtils.multiplyPercentage(amount, BalanceConstants.SUODESHUI)));
            withdrawRecord.setRate(BalanceConstants.SUODESHUI);
            withdrawRecord.setType(type.getCode());
            withdrawRecord.setUserId(user.getUserId());
            withdrawRecord.setStatus("0");
            withdrawRecord.setTenantId(user.getTenantId());
            this.save(withdrawRecord);
            callFeeRecordService.saveCallFeeRecord(user, WithdrawTypeEnum.HUAFEIFENCHENG, amount.negate(), "提现", withdrawRecord.getId());
            sysUserService.addBalance(BalanceConstants.CALL_BALANCE, amount.negate(), user.getUserId());
        }
        return Result.success("提交成功待审核");
    }

    private Boolean checkWithdrawal(WithdrawTypeEnum type) {
        LocalDate date = LocalDate.now();
        boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;
        // 判断是否是22号
        boolean isTwentySecond = date.getDayOfMonth() == BalanceConstants.TIXIAN_MONTH;
        return type.equals(WithdrawTypeEnum.YONGJIN) && !isSunday || type.equals(WithdrawTypeEnum.HUAFEIFENCHENG) && !isTwentySecond;
    }

    public Result handleStatus(TixianStatusDTO dto) {
        WithdrawRecord record = this.getById(dto.getId());
        if (! record.getTenantId().equals(SecurityUtils.getTenantId())) {
            return Result.error("非自己账号的数据");
        }
        SysUser user = sysUserService.getById(record.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (WithdrawStatusEnum.REJECT.getCode().equals(record.getStatus()) || WithdrawStatusEnum.PASS.getCode().equals(record.getStatus())) {
            return Result.error("已处理过了");
        }
        if (WithdrawStatusEnum.REJECT.getCode().equals(dto.getStatus())) {
            BigDecimal amount = record.getAmount();
            if (record.getType().equals(WithdrawTypeEnum.YONGJIN.getCode())) {
                callFeeRecordService.saveCallFeeRecord(user, WithdrawTypeEnum.YONGJIN, amount, "提现退回", record.getId());
                sysUserService.addBalance(BalanceConstants.BALANCE, amount, record.getUserId());
            } else if (record.getType().equals(WithdrawTypeEnum.HUAFEIFENCHENG.getCode())) {
                callFeeRecordService.saveCallFeeRecord(user, WithdrawTypeEnum.HUAFEIFENCHENG, amount, "提现退回", record.getId());
                sysUserService.addBalance(BalanceConstants.CALL_BALANCE, amount, record.getUserId());
            }
        }
        this.lambdaUpdate().eq(WithdrawRecord::getId, dto.getId()).set(WithdrawRecord::getStatus, dto.getStatus()).set(WithdrawRecord::getRemark, dto.getRemark()).update();
        return Result.success();
    }

    public List<WithdrawRecordVO> selectList(WithDrawRecordQuery query) {
        return this.baseMapper.selectListNew(query);
    }
}
