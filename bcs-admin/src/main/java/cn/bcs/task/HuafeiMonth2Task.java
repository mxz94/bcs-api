package cn.bcs.task;

import cn.bcs.common.constant.BalanceConstants;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysUserType;
import cn.bcs.common.utils.BigDecimalUtils;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.constants.ApplyStatus;
import cn.bcs.web.apply.domain.ApplyRecord;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import cn.bcs.web.apply.service.ApplyRecordService;
import cn.bcs.web.callFeeRecord.domain.CallFeeRecord;
import cn.bcs.web.callFeeRecord.service.CallFeeRecordService;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Component("huafeiMonthTask")
@Slf4j
public class HuafeiMonth2Task {

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
        for (ApplyRecord applyRecord : userMaxList) {
            Long betweenMonth = DateUtil.betweenMonth(now, applyRecord.getCreateTime(), true) -1;
            if (betweenMonth > 0) {
                if (betweenMonth.equals(BalanceConstants.noApplyMonth)) {
                    sysUserService.lambdaUpdate().eq(SysUser::getUserId, applyRecord.getUserId()).set(SysUser::getNoApplyMonth, betweenMonth).update();
                    // 连续3个月未开单， 清零余额
                    cleanFee(applyRecord.getUserId());
                } else {
                    sysUserService.lambdaUpdate().eq(SysUser::getUserId, applyRecord.getUserId()).set(SysUser::getNoApplyMonth, betweenMonth).update();
                }
            }
        }


        DateTime endTime =  DateUtil.beginOfMonth(new Date());
        String month = DateUtil.format(DateUtil.offsetMonth(endTime, -1), "yyyy-MM");
        log.info("开始执行话费分成任务，时间：" + month);
        // 计算话费分成
        List<MonthCallFeeVO> monthCallFeeVOS = applyRecordService.selectCallFee();
        log.info("开始执行话费分成任务，时间：" + monthCallFeeVOS);
        HashMap<Long, List<CallFeeRecord>> map = new HashMap<>();
        for (MonthCallFeeVO item : monthCallFeeVOS) {
            // 判断最近三个月是否成功开单， 没有则不进行话费分成
            callFeeRecordService.addRecordAndCallFee(item, month, map);
        }
        // 计算团队构建奖金
        for (Long teamUserId : map.keySet()) {
            callFeeRecordService.buildTeamFee(teamUserId, map.get(teamUserId), month);
        }
        List<SysUser> list = sysUserService.lambdaQuery().in(SysUser::getUserType, SysUserType.HEHUO.getCode(), SysUserType.DAILI.getCode()).isNull(SysUser::getFromUserId).list();
        for (SysUser user : list) {
            calculateTotalOrderAmount(user.getUserId())
        }
    }

    private void cleanFee(Long userId) {
        SysUser user = sysUserService.getById(userId);

        if (BigDecimalUtils.isGreaterThanZero(user.getCallBalance())) {
            callFeeRecordService.saveCallFeeRecord(user, WithdrawTypeEnum.HUAFEIFENCHENG, user.getCallBalance(), "连续未开单清零", null);
        }
        if (BigDecimalUtils.isGreaterThanZero(user.getTeamBuildBalance())) {
            callFeeRecordService.saveCallFeeRecord(user,WithdrawTypeEnum.TEAMBUILD, user.getCallBalance(), "连续未开单清零", null);
        }

        if (BigDecimalUtils.isGreaterThanZero(user.getBalance()) || BigDecimalUtils.isGreaterThanZero(user.getWaitInBalance())) {
            callFeeRecordService.saveCallFeeRecord(user,WithdrawTypeEnum.YONGJIN, user.getCallBalance(), "连续未开单清零", null);
        }
        sysUserService.lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getBalance, BigDecimal.ZERO)
                .set(SysUser::getWaitInBalance, BigDecimal.ZERO)
                .set(SysUser::getCallBalance, BigDecimal.ZERO)
                .set(SysUser::getTeamBuildBalance, BigDecimal.ZERO)
                .update();

        applyRecordService.lambdaUpdate().eq(ApplyRecord::getFromUserId, userId).eq(ApplyRecord::getStatus, ApplyStatus.APPROVED.getCode())
                .set(ApplyRecord::getStatus, ApplyStatus.ZUOFEI.getCode()).update();

    }


    private List<User> getDirectSubUsers(Integer userId) {
        List<User> subUsers = new ArrayList<>();
        for (User user : users) {
            if (userId.equals(user.getFromUserId())) {
                subUsers.add(user);
            }
        }
        return subUsers;
    }
    private Map<Integer, BigDecimal> userAmountMap = new HashMap<>();

    // 计算某个用户的订单总金额
    private BigDecimal getOrderAmountByUserId(Integer userId) {
        return orders.stream()
                .filter(order -> userId.equals(order.getUserId()))
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 递归计算每一层的订单金额（包括下级和自己）
    public BigDecimal calculateTotalOrderAmount(Integer userId) {
        // 1. 计算当前用户的订单金额
        BigDecimal currentUserOrderAmount = getOrderAmountByUserId(userId);

        // 2. 获取当前用户的直接下级
        List<User> subUsers = getDirectSubUsers(userId);

        // 3. 计算所有下级用户的订单金额
        BigDecimal subUsersOrderAmount = BigDecimal.ZERO;
        for (User subUser : subUsers) {
            // 每个下级用户调用递归计算自己的订单金额及其下级订单金额
            subUsersOrderAmount = subUsersOrderAmount.add(calculateTotalOrderAmount(subUser.getUserId()));
        }

        // 4. 当前用户及其所有下级的订单总金额
        BigDecimal totalAmount = currentUserOrderAmount.add(subUsersOrderAmount);

        // 5. 将结果保存到 Map 中
        userAmountMap.put(userId, totalAmount);

        return totalAmount;
    }
}
