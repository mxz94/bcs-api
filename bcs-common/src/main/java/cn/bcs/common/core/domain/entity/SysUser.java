package cn.bcs.common.core.domain.entity;

import cn.bcs.common.annotation.Excel;
import cn.bcs.common.annotation.Excel.Type;
import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseDBEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户账号
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phonenumber;

    /**
     * 帐号状态（0正常 1禁用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=禁用")
    private String status;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    @ApiModelProperty("上级代理ID")
    private Long fromUserId;

    @ApiModelProperty("已确认佣金")
    private BigDecimal balance;

    @ApiModelProperty("上周待确认佣金")
    private BigDecimal waitInBalance;

    @ApiModelProperty("话费抽成金额")
    private BigDecimal callBalance;

    @ApiModelProperty("团队构建金额")
    private BigDecimal teamBuildBalance;
}
