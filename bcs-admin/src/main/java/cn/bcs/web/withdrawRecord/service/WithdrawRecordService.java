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
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.third.domain.vo.WechatJsSdkSignVO;
import cn.bcs.web.withdrawRecord.constants.WithdrawStatusEnum;
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
    SysUserService sysUserService;
    public Result tixian(String type, BigDecimal amount) {
        SysUser user = sysUserService.getById(SecurityUtils.getUserId());
        if ("佣金".equals(type)) {
            if (BigDecimalUtils.isLessThan(user.getBalance(), amount)) {
                return Result.warn("佣金不足");
            }
            if (checkWithdrawal(type)) {
                return Result.warn("周日才可提现佣金");
            }
            if (BigDecimalUtils.isLessThan(amount, BalanceConstants.MIN_YONGJIN)) {
                return Result.warn("提现金额不能小于" + BalanceConstants.MIN_YONGJIN);
            }
            WithdrawRecord withdrawRecord = new WithdrawRecord();
            withdrawRecord.setOldBalance(user.getBalance());
            withdrawRecord.setAmount(amount);
            withdrawRecord.setRealAmount(BigDecimalUtils.multiplyPercentage(amount, BalanceConstants.SUODESHUI));
            withdrawRecord.setRate(BalanceConstants.SUODESHUI);
            withdrawRecord.setType(type);
            withdrawRecord.setUserId(user.getUserId());
            withdrawRecord.setStatus("0");
            this.save(withdrawRecord);
            sysUserService.addBalance(BalanceConstants.BALANCE, amount.negate(), user.getUserId());

        } else if ("话费分成".equals(type)) {
            if (BigDecimalUtils.isLessThan(user.getCallBalance(), amount)) {
                return Result.warn("话费分成不足");
            }
            if (checkWithdrawal(type)) {
                return Result.warn("每月22日提现花费分成");
            }
            if (BigDecimalUtils.isLessThan(amount, BalanceConstants.MIN_HUAFEI)) {
                return Result.warn("提现金额不能小于" + BalanceConstants.MIN_HUAFEI);
            }
            WithdrawRecord withdrawRecord = new WithdrawRecord();
            withdrawRecord.setOldBalance(user.getCallBalance());
            withdrawRecord.setAmount(amount);
            withdrawRecord.setRealAmount(BigDecimalUtils.multiplyPercentage(amount, BalanceConstants.SUODESHUI));
            withdrawRecord.setRate(BalanceConstants.SUODESHUI);
            withdrawRecord.setType(type);
            withdrawRecord.setUserId(user.getUserId());
            withdrawRecord.setStatus("0");
            this.save(withdrawRecord);
            sysUserService.addBalance(BalanceConstants.CALL_BALANCE, amount.negate(), user.getUserId());
        } else {
            return Result.warn("提现类型错误");
        }
        return Result.success("提交成功待审核");
    }

    private Boolean checkWithdrawal(String type) {
        LocalDate date = LocalDate.now();
        boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;
        // 判断是否是22号
        boolean isTwentySecond = date.getDayOfMonth() == 22;
        return "佣金".equals(type) && isSunday || "话费".equals(type) && isTwentySecond;
    }

    public int handleStatus(TixianStatusDTO dto) {
        WithdrawRecord record = this.getById(dto.getId());
        if (WithdrawStatusEnum.REJECT.getCode().equals(dto.getStatus())) {
            BigDecimal amount = record.getAmount();
            if (record.getType().equals("佣金")) {
                sysUserService.addBalance(BalanceConstants.BALANCE, amount, record.getUserId());
            } else if (record.getType().equals("话费分成")) {
                sysUserService.addBalance(BalanceConstants.CALL_BALANCE, amount, record.getUserId());
            }
        }
        this.lambdaUpdate().eq(WithdrawRecord::getId, dto.getId()).set(WithdrawRecord::getStatus, dto.getStatus()).set(WithdrawRecord::getRemark, dto.getRemark()).update();
        return 0;
    }

    public List<WithdrawRecordVO> selectList(WithDrawRecordQuery query) {
        return this.baseMapper.selectListNew(query);
    }
}
