package cn.bcs.web.third.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author m
 * @date 2022/5/25 10:37
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {
    /**
     * 微信appid
     */
    private String appid;
    /**
     * 微信secret
     */
    private String secret;
}
