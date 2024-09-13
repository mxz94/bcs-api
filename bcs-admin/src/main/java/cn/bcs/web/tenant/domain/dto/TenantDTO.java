package cn.bcs.web.tenant.domain.dto;

import cn.bcs.common.core.domain.model.Edit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhaoshuaixiang
 * @date 2023/5/15 18:03
 */
@Data
public class TenantDTO {

    @NotNull(message = "租户id不能为空", groups = {Edit.class})
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @NotBlank(message = "租户名称不能为空")
    @ApiModelProperty(value = "租户名称", required = true)
    private String tenantName;

    @NotNull(message = "租户角色不能为空")
    @ApiModelProperty(value = "租户角色")
    private Long roleId;

    @NotNull(message = "用户id不能为空", groups = {Edit.class})
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @ApiModelProperty(value = "用户数量限制")
    private Long userNumLimit;

    @ApiModelProperty(value = "密码")
    private String password;
}
