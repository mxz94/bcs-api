package cn.bcs.web.tenant.domain;

import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 租户对象 sys_tenant
 *
 * @author ruoyi
 * @date 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_tenant")
public class SysTenant extends BaseDBEntity {
private static final long serialVersionUID=1L;

    /** 租户id */
    @TableId(type = IdType.AUTO)
    private Long tenantId;

    /** 租户名称 */
    private String tenantName;

    /** 状态（0启用 1禁用） */
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 用户数量限制 */
    private Long userNumLimit;

    private String appid;

    private Object secret;

}
