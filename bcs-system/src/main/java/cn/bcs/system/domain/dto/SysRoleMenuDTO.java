package cn.bcs.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhaoshuaixiang
 * @date 2023/5/15 16:11
 */
@Data
public class SysRoleMenuDTO {

    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID列表")
    private List<Long> menuIds;
}
