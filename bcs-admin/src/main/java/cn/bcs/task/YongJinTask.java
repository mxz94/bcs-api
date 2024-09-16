package cn.bcs.task;

import cn.bcs.system.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class YongJinTask {

    @Resource
    private SysUserService sysUserService;
    // 每周一零晨 结算佣金到现金
    public void test() {
        sysUserService.addWaitBalance2Balance();
    }
}
