package cn.bcs.task;

import cn.bcs.common.constant.BalanceConstants;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.domain.ApplyRecord;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import cn.bcs.web.apply.service.ApplyRecordService;
import cn.bcs.web.callFeeRecord.constants.HuafeiRateUtils;
import cn.bcs.web.callFeeRecord.service.CallFeeRecordService;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("huafeiMonthTask")
@Slf4j
public class HuafeiMonthTask {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private ApplyRecordService applyRecordService;
    @Resource
    private CallFeeRecordService callFeeRecordService;

    // 每月1号凌晨执行
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        Date now = new Date();
        //查询系统内每个人最新审核通过的单子
        List<ApplyRecord> userMaxList = applyRecordService.selectMaxApplyRecord();
        List<Long> collect = sysUserService.lambdaQuery().isNull(SysUser::getFromUserId).in(SysUser::getUserType, SysUserType.HEHUO.getCode(), SysUserType.DAILI.getCode()).list().stream().map(SysUser::getUserId).collect(Collectors.toList());
        for (ApplyRecord applyRecord : userMaxList) {
            // 手动设置为合伙人的不删除
            if (applyRecord.getFromUserId() == null || collect.contains(applyRecord.getFromUserId())) {
                continue;
            }
            Long betweenMonth = DateUtil.betweenMonth(now, applyRecord.getCreateTime(), true) -1;
            if (betweenMonth > 0) {
                if (betweenMonth.equals(BalanceConstants.noApplyMonth)) {
                    sysUserService.lambdaUpdate().eq(SysUser::getUserId, applyRecord.getFromUserId()).set(SysUser::getNoApplyMonth, betweenMonth).update();
                    // 连续2个月未开单， 删除用户
                    applyRecordService.changeFromUserAndDel(applyRecord.getFromUserId());
                } else {
                    sysUserService.lambdaUpdate().eq(SysUser::getUserId, applyRecord.getFromUserId()).set(SysUser::getNoApplyMonth, betweenMonth).update();
                }
            }
        }

        log.info("开始执行话费分成任务，时间："  + System.currentTimeMillis());
        // 计算话费分成
        List<MonthCallFeeVO> monthCallFeeVOS = applyRecordService.selectCallFee();
        Map<Long, BigDecimal> userAmountMap = monthCallFeeVOS.stream()
                .collect(Collectors.toMap(
                        MonthCallFeeVO::getUserId,
                        MonthCallFeeVO::getAmount
                ));

        List<SysUser> list = sysUserService.lambdaQuery().in(SysUser::getUserType, SysUserType.HEHUO.getCode(), SysUserType.DAILI.getCode()).isNull(SysUser::getFromUserId).list();
        for (SysUser user : list) {
            calculateTotalOrderAmount(user, userAmountMap);
        }
        log.info("开始执行话费分成任务，时间：" + System.currentTimeMillis());
    }

    private List<SysUser> getDirectSubUsers(Long userId) {
        return sysUserService.lambdaQuery().eq(SysUser::getFromUserId, userId).in(SysUser::getUserType, SysUserType.HEHUO.getCode(), SysUserType.DAILI.getCode()).list();
    }

    // 递归计算每一层的订单金额（包括下级和自己）
    public SysUser calculateTotalOrderAmount(SysUser user, Map<Long, BigDecimal> userAmountMap) {
        // 1. 计算当前用户的订单金额
        BigDecimal currentUserAmount = userAmountMap.get(user.getUserId());

        // 2. 获取当前用户的直接下级
        List<SysUser> subUsers = getDirectSubUsers(user.getUserId());
        if (CollectionUtils.isEmpty(subUsers) && BigDecimalUtils.isNullOrLessOrEqZero(currentUserAmount)) {
            user.setHuafeiTeamTotal(BigDecimal.ZERO);
            user.setHuafeiTeamTotalRate(BigDecimal.ZERO);
            user.setHuafeiSubFenTotal(BigDecimal.ZERO);
            user.setHuafeiTeamFen(BigDecimal.ZERO);
            return user;
        }

        // 3. 计算所有下级用户的订单金额
        BigDecimal subHuaFeiTotal = BigDecimal.ZERO;
        BigDecimal subHuaFeiFenTotal = BigDecimal.ZERO;
        for (SysUser subUser : subUsers) {
            // 每个下级用户调用递归计算自己的订单金额及其下级订单金额
            SysUser su = calculateTotalOrderAmount(subUser, userAmountMap);
            subHuaFeiTotal = subHuaFeiTotal.add(su.getHuafeiTeamTotal());
            // 累计当前下级的话费分成总和
            subHuaFeiFenTotal = subHuaFeiFenTotal.add(HuafeiRateUtils.calculateTaxAmount(su.getHuafeiTeamTotal(), su.getHuafeiTeamTotalRate()));
        }
        // 计算汇率金额前计算当前用户费率
        if (user.getFromUserId() == null) {
            List<SysUser> list = sysUserService.lambdaQuery().eq(SysUser::getOldFromUserId, user.getUserId()).list();
            if (list.size() > 0) {
                user.setHuafeiTeamTotalRate(BalanceConstants.MAX_RATE_38);
                if (list.size() > 1) {
                    for (SysUser sysUser : list.subList(1, list.size())) {
                        callFeeRecordService.addGongxianAndCallFee(user, sysUser);
                    }
                }
            }
        }

        // 4. 当前用户及其所有下级的订单总金额
        BigDecimal totalAmount = BigDecimalUtils.add(currentUserAmount, subHuaFeiTotal);
        user.setHuafeiTeamTotal(totalAmount);
        if (!BalanceConstants.MAX_RATE_38.equals(user.getHuafeiTeamTotalRate())) {
            user.setHuafeiTeamTotalRate(HuafeiRateUtils.calculateTaxRate(totalAmount));
        }
        user.setHuafeiSubFenTotal(subHuaFeiFenTotal);
        user.setHuafeiTeamFen(BigDecimalUtils.subtract(HuafeiRateUtils.calculateTaxAmount(totalAmount, user.getHuafeiTeamTotalRate()), subHuaFeiFenTotal));
        callFeeRecordService.addRecordAndCallFee(user);
        // 杰出贡献奖励 大于125000
        // 1. 修改自己的推荐人为null。 oldFromUserId;
        // 2. 修改上级的汇率38%
        if (BigDecimalUtils.isGreaterThanOrEqual(user.getHuafeiTeamTotal(), BalanceConstants.GONGXIAN_125000)) {
            sysUserService.lambdaUpdate().eq(SysUser::getUserId, user.getUserId()).set(SysUser::getFromUserId, null)
                    .set(SysUser::getOldFromUserId, user.getFromUserId())
                    .update();
            user.setHuafeiTeamTotal(BigDecimal.ZERO);
            user.setHuafeiTeamTotalRate(BigDecimal.ZERO);
            user.setHuafeiSubFenTotal(BigDecimal.ZERO);
            user.setHuafeiTeamFen(BigDecimal.ZERO);
            return user;
        }
        // 用户的话费分成
        return user;
    }
}
