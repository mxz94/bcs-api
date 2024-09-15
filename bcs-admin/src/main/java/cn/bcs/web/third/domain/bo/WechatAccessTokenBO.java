package cn.bcs.web.third.domain.bo;

import lombok.Data;

/**
 * @author m
 * @date 2022/5/25 14:46
 */
@Data
public class WechatAccessTokenBO {
    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    private String access_token;
    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private Integer expires_in;
    /**
     * 用户刷新access_token
     */
    private String refresh_token;
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;
    /**
     * 获得is_snapshotuser==1（用户是快照模式进入）
     */
    private Integer is_snapshotuser;
}
