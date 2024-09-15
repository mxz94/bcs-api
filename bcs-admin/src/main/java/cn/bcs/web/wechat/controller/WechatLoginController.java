package cn.bcs.web.wechat.controller;

import cn.bcs.common.annotation.Log;
import cn.bcs.common.constant.HttpStatus;
import cn.bcs.common.core.domain.Result;
import cn.bcs.system.domain.vo.LoginVO;
import cn.bcs.web.third.domain.bo.WechatAccessTokenBO;
import cn.bcs.web.third.domain.vo.WechatJsSdkSignVO;
import cn.bcs.web.third.support.WechatSupport;
import cn.bcs.web.wechat.service.WechatLoginService;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "微信")
@RestController
public class WechatLoginController {
    @Resource
    private WechatLoginService loginService;
    @Resource
    private WechatSupport wechatSupport;

    @Log(title = "微信登陆")
    @ApiOperation(value = "微信授权登录")
    @PostMapping("/h5/wechatLogin")
    public Result<LoginVO> wechatLogin(@RequestParam String code) {
        WechatAccessTokenBO accessToken = wechatSupport.getAccessToken(code);
        if (BeanUtil.isEmpty(accessToken)) {
            return Result.error("微信授权失败");
        }
        if (accessToken.getIs_snapshotuser() != null && 1 == accessToken.getIs_snapshotuser()) {
            return Result.error(HttpStatus.WECHAT_SNAPSHOTUSER, "请使用完整服务");
        }
        return loginService.wechatLogin1(accessToken);
    }

    @ApiOperation(value = "获取公众号网页配置参数", notes = "用于页面调用微信扫一扫")
    @ApiImplicitParam(name = "url", value = "网址URL", required = true)
    @GetMapping("/h5/getConfig")
    public Result<WechatJsSdkSignVO> getConfig(@RequestParam String url) {
        return Result.success(wechatSupport.jsSdkSign(url));
    }
}