package cn.bcs.system.domain.dto;

import cn.bcs.common.enums.SysCommonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhaoshuaixiang
 * @date 2023/5/10 11:08
 */
@Data
public class SysUserStatusDTO {

    @NotBlank(message = "请选择状态")
    @ApiModelProperty(value = "状态 " + SysCommonStatus.INFO, required = true)
    private String status;

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private List<Long> userIds;
}
