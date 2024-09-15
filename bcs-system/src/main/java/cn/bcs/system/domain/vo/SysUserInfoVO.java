package cn.bcs.system.domain.vo;

import cn.bcs.common.core.domain.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author m
 * @date 2023/5/10 09:50
 */
@Accessors(chain = true)
@Data
public class SysUserInfoVO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "角色列表")
    private List<SysRole> roles;
}
