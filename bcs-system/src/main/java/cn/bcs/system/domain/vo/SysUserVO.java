package cn.bcs.system.domain.vo;

import cn.bcs.common.enums.SysCommonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author m
 * @date 2023/5/9 16:29
 */
@Data
public class SysUserVO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "帐号状态 " + SysCommonStatus.INFO)
    private String status;

    @ApiModelProperty(value = "是否有指纹")
    private Boolean fingerprintFlag;

    @ApiModelProperty(value = "是否有刷卡")
    private Boolean cardFlag;

    @ApiModelProperty(value = "是否有人脸")
    private Boolean faceFlag;

    @ApiModelProperty(value = "创建时间")
    private String createTime;


}
