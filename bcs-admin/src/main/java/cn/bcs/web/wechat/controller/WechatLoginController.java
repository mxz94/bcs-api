package cn.bcs.web.wechat.controller;

import cn.bcs.common.annotation.Log;
import cn.bcs.common.constant.HttpStatus;
import cn.bcs.common.core.domain.Result;
import cn.bcs.system.domain.vo.LoginVO;
import cn.bcs.web.third.domain.bo.WechatAccessTokenBO;
import cn.bcs.web.third.domain.vo.WechatJsSdkSignVO;
import cn.bcs.web.third.support.WechatSupport;
import cn.bcs.web.wechat.domain.vo.WechatUserInfo;
import cn.bcs.web.wechat.service.WechatLoginService;
import cn.bcs.web.withdrawRecord.constants.WithdrawStatusEnum;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import cn.bcs.web.withdrawRecord.domain.dto.TixianDTO;
import cn.bcs.web.withdrawRecord.service.WithdrawRecordService;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(tags = "公众号")
@RestController
@RequestMapping("/wechat")
public class WechatLoginController {
    @Resource
    private WechatLoginService loginService;
    @Resource
    private WechatSupport wechatSupport;
    @Resource
    private WithdrawRecordService withdrawRecordService;

    @ApiOperation(value = "用户信息")
    @GetMapping("/userInfo")
    public Result<WechatUserInfo> userInfo() {
        return loginService.userInfo();
    }

    @Log(title = "微信登陆")
    @ApiOperation(value = "微信授权登录")
    @PostMapping("/h5/wechatLogin")
    public Result<LoginVO> wechatLogin(@RequestParam String code, @RequestParam String tenantId) {
        WechatAccessTokenBO accessToken = wechatSupport.getAccessToken(code);
        if (BeanUtil.isEmpty(accessToken)) {
            return Result.error("微信授权失败");
        }
        if (accessToken.getIs_snapshotuser() != null && 1 == accessToken.getIs_snapshotuser()) {
            return Result.error(HttpStatus.WECHAT_SNAPSHOTUSER, "请使用完整服务");
        }
        return loginService.wechatLogin1(accessToken, tenantId);
    }

    @ApiOperation(value = "获取公众号网页配置参数", notes = "用于页面调用微信扫一扫")
    @ApiImplicitParam(name = "url", value = "网址URL", required = true)
    @GetMapping("/h5/getConfig")
    public Result<WechatJsSdkSignVO> getConfig(@RequestParam String url) {
        return Result.success(wechatSupport.jsSdkSign(url));
    }

}