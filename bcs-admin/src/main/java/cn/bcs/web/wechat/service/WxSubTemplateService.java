package cn.bcs.web.wechat.service;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.web.third.support.WechatSupport;
import cn.bcs.web.wechat.domain.WxSubTemplate;
import cn.bcs.web.wechat.mapper.WxSubTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (WxSubTemplate)表服务实现类
 *
 * @author mxz
 * @since 2024-01-17 15:17:57
 */
@Service("wxSubTemplateService")
public class WxSubTemplateService extends ServiceImpl<WxSubTemplateMapper, WxSubTemplate> {

    @Resource
    WechatSupport wechatSupport;

    public Result addSubTemplate(String deptId) {
        Integer count = this.lambdaQuery().eq(WxSubTemplate::getTenantId, deptId).count();
        if (count > 0) {
            return Result.error("已配置过");
        }
        List<WxSubTemplate> tempplateList = wechatSupport.addSubTempplate(deptId);
        this.saveBatch(tempplateList);
        return Result.success();
    }

    public Result<List<String>> getSubTemplateList() {
        Long tenantId = SecurityUtils.getLoginUser().getTenantId();
        List<String> subTemplateList = this.lambdaQuery().eq(WxSubTemplate::getTemplateId, tenantId).list().stream().map(WxSubTemplate::getTemplateId).collect(Collectors.toList());
        return Result.success(subTemplateList);
    }
}

