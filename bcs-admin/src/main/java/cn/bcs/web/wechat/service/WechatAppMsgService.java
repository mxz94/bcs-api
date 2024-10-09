package cn.bcs.web.wechat.service;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.framework.manager.AsyncManager;
import cn.bcs.system.service.SysUserService;
import cn.bcs.web.third.support.WechatSupport;
import cn.bcs.web.wechat.domain.WxSubTemplate;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 微信发送消息
 *
 * @author mxz
 * @since 2022-10-22 16:06:59
 */
@Service
public class WechatAppMsgService {

    @Resource
    SysUserService sysUserService;

    @Resource
    WechatSupport wechatSupport;
    @Resource
    WxSubTemplateService wxSubTemplateService;

    private void setKey(JSONObject data, String key, String value) {
        JSONObject vj = new JSONObject();
        vj.set("value", value);
        data.set(key, vj);
    }

    //public void asyncSend(Object order, String  msgType) {
    //    AsyncManager.me().asyncExecute(() -> pushSubscriptionMsg(order, msgType));
    //}

    public Result<String> pushAmountChange(Long userId, String amount, String type) {
        SysUser user = sysUserService.getById(userId);
        WxSubTemplate one = wxSubTemplateService.lambdaQuery().eq(WxSubTemplate::getTenantId, user.getTenantId()).eq(WxSubTemplate::getType, "账户余额变更通知").one();
        String templateId = one.getTemplateId();
        JSONObject data = new JSONObject();
        setKey(data, "amount1", amount);
        setKey(data, "amount3", "");
        setKey(data, "amount5", type);
        setKey(data, "time4", DateUtil.now());
        String s = wechatSupport.sendSubscriberMessage(user.getUserName().split(":")[0], templateId, data, "", user.getTenantId());
        return Result.success(s);
    }
}

