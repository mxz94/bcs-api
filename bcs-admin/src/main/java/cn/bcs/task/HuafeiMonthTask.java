package cn.bcs.task;

import cn.bcs.system.service.SysUserService;
import cn.bcs.web.apply.domain.vo.MonthCallFeeVO;
import cn.bcs.web.apply.service.ApplyRecordService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class HuafeiMonthTask {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private ApplyRecordService applyRecordService;
    // 每周一零晨 结算佣金到现金
    public void test() {
        List<MonthCallFeeVO> monthCallFeeVOS = applyRecordService.selectCallFee();
        for (MonthCallFeeVO item : monthCallFeeVOS) {
            //
        }
    }
}
