package cn.bcs.common.core.domain.entity;

import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Accessors(chain = true)
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRole extends BaseDBEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
    /**
     * 角色权限
     */
    private String roleKey;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
    /**
     * 备注
     */
    private String remark;
}
