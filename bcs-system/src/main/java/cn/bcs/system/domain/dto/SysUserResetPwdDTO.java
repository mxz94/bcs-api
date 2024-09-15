package cn.bcs.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author m
 * @date 2023/5/10 11:34
 */
@Data
public class SysUserResetPwdDTO {

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private List<Long> userIds;
}
