package cn.bcs.web.callFeeRecord.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bcs.common.constant.BalanceConstants;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.service.ApplyRecordService;
import cn.bcs.web.callFeeRecord.constants.HuafeiRateUtils;
import cn.bcs.web.callFeeRecord.domain.vo.CallFeeRecordVO;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.bcs.web.callFeeRecord.domain.query.RecordQuery;
import cn.hutool.core.date.DateUtil;
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
    public void addRecordAndCallFee(SysUser user) {
        String month = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -1), "yyyy-MM");
        BigDecimal callBalance = user.getCallBalance();
        BigDecimal newCallBalance = BigDecimalUtils.add(callBalance, user.getHuafeiTeamFen());
        userService.lambdaUpdate().eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getCallBalance, newCallBalance)
                .set(SysUser::getHuafeiTeamTotal, user.getHuafeiTeamTotal())
                .set(SysUser::getHuafeiTeamTotalRate, user.getHuafeiTeamTotalRate())
                .set(SysUser::getHuafeiSubFenTotal, user.getHuafeiSubFenTotal())
                .set(SysUser::getHuafeiTeamFen, user.getHuafeiTeamFen())
                .update();
        if (BigDecimalUtils.isNullOrLessZero(user.getHuafeiTeamFen())) {
            return;
        }
        CallFeeRecord record = new CallFeeRecord();
        record.setHuafeiTeamTotal(user.getHuafeiTeamTotal());
        record.setHuafeiTeamTotalRate(user.getHuafeiTeamTotalRate());
        record.setHuafeiSubFenTotal(user.getHuafeiSubFenTotal());
        record.setFee(user.getHuafeiTeamFen());
        record.setUserId(user.getUserId());
        record.setOldBalance(callBalance);
        record.setNewBalance(newCallBalance);
        record.setMonth(month);
        record.setType(WithdrawTypeEnum.HUAFEIFENCHENG.getCode());
        record.setTenantId(user.getTenantId());
        this.save(record);
    }

    public void saveCallFeeRecord(SysUser user, WithdrawTypeEnum type, BigDecimal balance, String remark, Long recordId) {
        BigDecimal oldBalance = BigDecimal.ZERO;
        if (WithdrawTypeEnum.YONGJIN.equals(type)) {
            oldBalance = BigDecimalUtils.add(user.getBalance(), user.getWaitInBalance());
        } else if (WithdrawTypeEnum.HUAFEIFENCHENG.equals(type)) {
            oldBalance = user.getCallBalance();
        }
        CallFeeRecord record = new CallFeeRecord();
        record.setRecordId(recordId);
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
