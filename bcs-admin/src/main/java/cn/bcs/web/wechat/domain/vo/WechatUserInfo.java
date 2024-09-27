package cn.bcs.web.wechat.domain.vo;

import cn.bcs.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WechatUserInfo extends SysUser {
    @ApiModelProperty(value = "推荐人")
    private String fromUserNickName;
    @ApiModelProperty(value = "分享链接")
    private String shareUrl;

    @ApiModelProperty(value = "总佣金")
    private BigDecimal allBalance;
}
