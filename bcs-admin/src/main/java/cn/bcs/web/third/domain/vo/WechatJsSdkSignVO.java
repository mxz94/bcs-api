package cn.bcs.web.third.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author m
 * @date 2022/3/28 13:49
 */
@Data
@Accessors(chain = true)
public class WechatJsSdkSignVO {

    @ApiModelProperty(value = "微信appid")
    private String appId;

    @ApiModelProperty(value = "timestamp")
    private String timestamp;

    @ApiModelProperty(value = "nonceStr")
    private String nonceStr;

    @ApiModelProperty(value = "signature")
    private String signature;
}
