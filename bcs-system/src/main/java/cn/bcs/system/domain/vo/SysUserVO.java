package cn.bcs.system.domain.vo;

import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.common.enums.SysCommonStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author m
 * @date 2023/5/9 16:29
 */
@Data
public class SysUserVO extends SysUser {
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "推荐人")
    private String fromNickName;
}
