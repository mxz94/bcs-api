package cn.bcs.system.domain.dto;

import cn.bcs.common.core.domain.model.Edit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author m
 * @date 2023/5/9 16:29
 */
@Data
public class SysUserDTO {

    @NotNull(groups = {Edit.class}, message = "用户id不能为空")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Size(max = 120, message = "用户名长度不能超过120个字符")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Size(max = 120, message = "密码长度不能超过120个字符")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @Size(max = 120, message = "手机号码长度不能超过120个字符")
    @ApiModelProperty(value = "手机号码")
    private String phonenumber;


    @ApiModelProperty(value = "是否欠费")
    private Integer qianfei;

}
