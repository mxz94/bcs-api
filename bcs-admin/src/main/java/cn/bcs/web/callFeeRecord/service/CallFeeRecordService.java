package cn.bcs.web.callFeeRecord.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bcs.common.constant.BalanceConstants;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import cn.bcs.web.apply.service.ApplyRecordService;
import cn.bcs.web.callFeeRecord.constants.HuafeiRateUtils;
import cn.bcs.web.callFeeRecord.domain.vo.CallFeeRecordVO;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.bcs.web.callFeeRecord.domain.query.RecordQuery;
import org.springframework.stereotype.Service;
import cn.bcs.web.callFeeRecord.domain.CallFeeRecord;
import cn.bcs.web.callFeeRecord.mapper.CallFeeRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 话费分成记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Service
public class  CallFeeRecordService extends ServiceImpl<CallFeeRecordMapper, CallFeeRecord> {

    @Resource
    SysUserService userService;
    @Resource
    ApplyRecordService applyRecordService;
    @Transactional(rollbackFor = Exception.class)
    public void addRecordAndCallFee(MonthCallFeeVO item, String month, HashMap<Long, List<CallFeeRecord>> map) {
        SysUser user = userService.getById(item.getUserId());
        if (user == null && user.getNoApplyMonth() != null && user.getNoApplyMonth() >= BalanceConstants.noApplyMonth) {
            return;
        }
        BigDecimal callBalance = user.getCallBalance();
        BigDecimal fee = HuafeiRateUtils.calculateTaxAmount(item.getAmount());

        userService.addBalance(BalanceConstants.CALL_BALANCE, fee, item.getUserId());

        CallFeeRecord record = new CallFeeRecord();
        record.setRecordIds(item.getRecordIds());
        record.setSumCallFee(item.getAmount());
        record.setRate(HuafeiRateUtils.calculateTaxRate(callBalance));
        record.setFee(fee);
        record.setUserId(item.getUserId());
        record.setOldBalance(callBalance);
        record.setNewBalance(BigDecimalUtils.add(callBalance, fee));
        record.setMonth(month);
        record.setType(WithdrawTypeEnum.HUAFEIFENCHENG.getCode());
        record.setTenantId(user.getTenantId());
        this.save(record);
        if (user.getFromUserId() != null) {
            List<CallFeeRecord> records = map.computeIfAbsent(user.getFromUserId(), k -> new ArrayList<>());
            records.add(record);
        }
    }

    public void buildTeamFee(Long teamUserId, List<CallFeeRecord> callFeeRecords, String month) {
        SysUser teamUser = userService.getById(teamUserId);
        if (teamUser == null && teamUser.getNoApplyMonth() != null && teamUser.getNoApplyMonth() >= BalanceConstants.noApplyMonth) {
            return;
        }

        BigDecimal sumFee = BigDecimal.ZERO;
        BigDecimal fenFee = BigDecimal.ZERO;
        List<Long> rs = new ArrayList<>();
        for (CallFeeRecord callFeeRecord : callFeeRecords) {
            sumFee = BigDecimalUtils.add(sumFee, callFeeRecord.getSumCallFee());
            fenFee = BigDecimalUtils.add(fenFee, callFeeRecord.getFee());
            rs.add(callFeeRecord.getId());
        }
        BigDecimal rate = HuafeiRateUtils.calculateTaxRate(sumFee);
        BigDecimal fee = BigDecimalUtils.subtract(HuafeiRateUtils.calculateTaxAmount(sumFee), fenFee);
        if (BigDecimalUtils.isNullOrLessOrEqZero(fee)) {
            return;
        }

        BigDecimal callBalance = teamUser.getCallBalance();

        userService.addBalance(BalanceConstants.TEAM_BUILD_BALANCE, fee, teamUser.getUserId());

        CallFeeRecord record = new CallFeeRecord();
        record.setRecordIds(StringUtils.join( rs,","));
        record.setSumCallFee(sumFee);
        record.setRate(rate);
        record.setFee(fee);
        record.setUserId(teamUser.getUserId());
        record.setOldBalance(callBalance);
        record.setNewBalance(BigDecimalUtils.add(callBalance, fee));
        record.setMonth(month);
        record.setTenantId(teamUser.getTenantId());
        record.setType(WithdrawTypeEnum.TEAMBUILD.getCode());
        record.setRemark(StringUtils.format(WithdrawTypeEnum.TEAMBUILD.getDesc() + ":【{}(团队总话费)*{}%-{}(团队个人分成总和)】", sumFee, rate, fenFee ));
        this.save(record);
    }

    public void saveCallFeeRecord(SysUser user, WithdrawTypeEnum type, BigDecimal balance, String remark, Long recordId) {
        BigDecimal oldBalance = BigDecimal.ZERO;
        if (WithdrawTypeEnum.YONGJIN.equals(type)) {
            oldBalance = BigDecimalUtils.add(user.getBalance(), user.getWaitInBalance());
        } else if (WithdrawTypeEnum.HUAFEIFENCHENG.equals(type)) {
            oldBalance = user.getCallBalance();
        } else if (WithdrawTypeEnum.TEAMBUILD.equals(type)) {
            oldBalance = user.getTeamBuildBalance();
        }
        CallFeeRecord record = new CallFeeRecord();
        record.setRecordIds(recordId != null ? String.valueOf(recordId) : null);
        record.setSumCallFee(BigDecimal.ZERO);
        record.setRate(BigDecimal.ZERO);
        record.setFee(balance);
        record.setUserId(user.getUserId());
        record.setOldBalance(oldBalance);
        record.setNewBalance(BigDecimalUtils.add(oldBalance, balance));
        record.setType(type.getCode());
        record.setRemark(remark);
        record.setTenantId(user.getTenantId());
        this.save(record);
    }

    public List<CallFeeRecordVO> selectRecordListByType(RecordQuery query) {
        List<CallFeeRecordVO> list = this.baseMapper.selectRecordListByType(query);
        return list;
    }
}
