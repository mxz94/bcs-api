package cn.bcs.web.tenant.domain.vo;

import cn.bcs.common.core.domain.entity.SysRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author m
 * @date 2023/5/15 18:03
 */
@Data
public class TenantVO {

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "状态（0启用 1禁用）")
    private String status;

    @ApiModelProperty(value = "用户数量限制")
    private Long userNumLimit;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "角色列表")
    private List<SysRole> roles;
}
