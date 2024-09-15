package cn.bcs.web.third.support;

import cn.bcs.common.core.redis.RedisCache;
import cn.bcs.common.exception.CustomException;
import cn.bcs.web.third.config.WechatConfig;
import cn.bcs.web.third.domain.bo.WechatAccessTokenBO;
import cn.bcs.web.third.domain.bo.WechatUserInfoBO;
import cn.bcs.web.third.domain.vo.WechatJsSdkSignVO;
import cn.bcs.web.third.service.ThirdRequestRecordService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信支持
 *
 * @author m
 * @date 2022/5/25 10:29
 */
@Slf4j
@Service
public class WechatSupport {
    @Resource
    private ThirdRequestRecordService thirdRequestRecordService;
    @Resource
    private WechatConfig wechatConfig;
    @Resource
    private RedisCache redisCache;

    static String ACCESS_TOKEN = StrUtil.EMPTY;
    static String JSAPI_TICKET = StrUtil.EMPTY;

    /**
     * 根据微信code获取用户信息
     *
     * @param code 微信页面获取code
     * @return WechatUserInfoBO
     */
    public WechatUserInfoBO codeGetUserinfo(String code) {
        WechatAccessTokenBO accessTokenBO = getAccessToken(code);
        if (BeanUtil.isEmpty(accessTokenBO)) {
            return new WechatUserInfoBO();
        }
        return getUserinfo(accessTokenBO.getAccess_token(), accessTokenBO.getOpenid());
    }

    /**
     * 通过 code 换取网页授权access_token
     *
     * @param code 微信页面获取code
     * @return WechatAccessTokenBO
     */
    public WechatAccessTokenBO getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appid", wechatConfig.getAppid());
        paramMap.put("secret", wechatConfig.getSecret());
        paramMap.put("code", code);
        paramMap.put("grant_type", "authorization_code");
        String result = HttpUtil.get(url, paramMap);
        thirdRequestRecordService.saveRecords(url, "微信通过code换取网页授权access_token", paramMap.toString(), result);
        if (StringUtils.isEmpty(result)) {
            log.error("获取微信access_token失败{}", result);
            throw new CustomException("获取微信access_token失败");
        }
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject.containsKey("errcode")) {
            log.error("获取微信access_token失败{}", result);
            throw new CustomException("获取微信access_token失败:" + jsonObject.get("errmsg"));
        }
        return JSONUtil.toBean(result, WechatAccessTokenBO.class);
    }

    /**
     * 拉取用户信息
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openid      用户的唯一标识
     * @return WechatUserInfoBO
     */
    public WechatUserInfoBO getUserinfo(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("access_token", accessToken);
        paramMap.put("openid", openid);
        paramMap.put("lang", "zh_CN");
        String result = HttpUtil.get(url, paramMap);
        thirdRequestRecordService.saveRecords(url, "微信拉取用户信息", paramMap.toString(), result);
        if (StringUtils.isEmpty(result)) {
            log.error("获取微信用户信息失败{}", result);
            throw new CustomException("获取微信用户信息失败");
        }
        return JSONUtil.toBean(result, WechatUserInfoBO.class);
    }

    /**
     * 获取 jsSdkSign
     *
     * @param url url
     * @return WechatJsSdkSignVO
     */
    public WechatJsSdkSignVO jsSdkSign(String url) {
        String nonceStr = IdUtil.simpleUUID();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        // 注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + getJsApiTicket() + "&noncestr=" + nonceStr
                + "&timestamp=" + timestamp + "&url=" + url;
        String signature = SecureUtil.sha1(string1);
        return new WechatJsSdkSignVO()
                .setAppId(wechatConfig.getAppid())
                .setTimestamp(timestamp)
                .setNonceStr(nonceStr)
                .setSignature(signature);
    }

    /**
     * H5页面接入
     */
    public String getJsApiTicket() {
        if ("dev".equals(SpringUtil.getActiveProfile())) {
            // 测试环境不刷新
            return JSAPI_TICKET;
        }
        if (redisCache.hasKey("wechat_jsapi_ticket")) {
            JSAPI_TICKET = redisCache.getCacheObject("wechat_jsapi_ticket");
            if (StringUtils.hasText(JSAPI_TICKET)) {
                return JSAPI_TICKET;
            }
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("type", "jsapi");
        paramMap.put("access_token", getAccessToken());
        String result = HttpUtil.get(url, paramMap);
        log.info("获取微信JsApiTicket：{}", result);
        if (StringUtils.isEmpty(result)) {
            throw new CustomException("获取微信JsApiTicket失败");
        }
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject.containsKey("errcode") && jsonObject.getInt("errcode").equals(1)) {
            throw new CustomException("获取微信JsApiTicket失败:" + jsonObject.get("errmsg"));
        }
        JSAPI_TICKET = JSONUtil.parseObj(result).getStr("ticket");
        redisCache.setCacheObject("wechat_jsapi_ticket", JSAPI_TICKET, 7000, TimeUnit.SECONDS);
        return JSAPI_TICKET;
    }

    /**
     * 获取微信公众号AccessToken
     */
    public String getAccessToken() {
        if ("dev".equals(SpringUtil.getActiveProfile())) {
            // 测试环境不刷新
            return ACCESS_TOKEN;
        }
        if (redisCache.hasKey("wechat_access_token")) {
            ACCESS_TOKEN = redisCache.getCacheObject("wechat_access_token");
            if (StringUtils.hasText(ACCESS_TOKEN)) {
                return ACCESS_TOKEN;
            }
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, Object> paramMap = new HashMap<>(3);
        paramMap.put("grant_type", "client_credential");
        paramMap.put("appid", wechatConfig.getAppid());
        paramMap.put("secret", wechatConfig.getSecret());
        String result = HttpUtil.get(url, paramMap);
        log.info("获取微信接口调用凭证AccessToken：{}", result);
        if (StringUtils.isEmpty(result)) {
            throw new CustomException("获取微信接口调用凭证AccessToken失败");
        }
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject.containsKey("errcode")) {
            throw new CustomException("获取微信接口调用凭证AccessToken失败:" + jsonObject.get("errmsg"));
        }
        ACCESS_TOKEN = JSONUtil.parseObj(result).getStr("access_token");
        redisCache.setCacheObject("wechat_access_token", ACCESS_TOKEN, 7000, TimeUnit.SECONDS);
        return ACCESS_TOKEN;
    }
}
