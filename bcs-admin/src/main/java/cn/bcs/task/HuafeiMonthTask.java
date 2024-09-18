package cn.bcs.task;

import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.constants.ApplyStatus;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import cn.bcs.web.apply.service.ApplyRecordService;
import cn.bcs.web.callFeeRecord.domain.CallFeeRecord;
import cn.bcs.web.callFeeRecord.service.CallFeeRecordService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import cn.bcs.web.apply.domain.ApplyRecord;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component("huafeiMonthTask")
@Slf4j
public class HuafeiMonthTask {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private ApplyRecordService applyRecordService;
    @Resource
    private CallFeeRecordService callFeeRecordService;

    // 每周一零晨 结算佣金到现金
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        DateTime endTime =  DateUtil.beginOfMonth(new Date());
        DateTime startTime = DateUtil.offsetMonth(endTime, -3);
        String month = DateUtil.format(DateUtil.offsetMonth(endTime, -1), "yyyy-MM");
        log.info("开始执行话费分成任务，时间：" + month);
        // 计算话费分成
        List<MonthCallFeeVO> monthCallFeeVOS = applyRecordService.selectCallFee();
        log.info("开始执行话费分成任务，时间：" + monthCallFeeVOS);
        HashMap<Long, List<CallFeeRecord>> map = new HashMap<>();
        for (MonthCallFeeVO item : monthCallFeeVOS) {
            // 判断最近两个月是否成功开单， 没有则不进行话费分成
            Integer count = applyRecordService.lambdaQuery().eq(ApplyRecord::getUserId, item.getUserId())
                    .gt(ApplyRecord::getCreateTime, startTime)
                    .lt(ApplyRecord::getCreateTime, endTime)
                    .eq(ApplyRecord::getStatus, ApplyStatus.APPROVED.getCode()).count();
            if (count == 0) {
                continue;
            }
            callFeeRecordService.addRecordAndCallFee(item, month, map);
        }
        // 计算团队构建奖金
        for (Long teamUserId : map.keySet()) {
            Integer count = applyRecordService.lambdaQuery().eq(ApplyRecord::getUserId, teamUserId)
                    .gt(ApplyRecord::getCreateTime, startTime)
                    .lt(ApplyRecord::getCreateTime, endTime)
                    .eq(ApplyRecord::getStatus, ApplyStatus.APPROVED.getCode()).count();
            if (count == 0) {
                continue;
            }
            callFeeRecordService.buildTeamFee(teamUserId, map.get(teamUserId), month);
        }
    }
}
