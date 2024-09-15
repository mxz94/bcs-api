package cn.bcs.system.domain.query;

import cn.bcs.common.core.page.PageDomain;
import cn.bcs.common.enums.SysCommonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author m
 * @date 2023/5/9 16:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserQuery extends PageDomain {
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "帐号状态 " + SysCommonStatus.INFO)
    private String status;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
