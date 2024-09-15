package cn.bcs.web.tenant.domain.query;

import cn.bcs.common.core.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author m
 * @date 2023/5/17 18:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantQuery extends PageDomain {

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    @ApiModelProperty(value = "用户名")
    private String userName;
}
