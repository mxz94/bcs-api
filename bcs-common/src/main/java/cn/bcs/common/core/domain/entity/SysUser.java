package cn.bcs.common.core.domain.entity;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.annotation.Excel;
import cn.bcs.common.annotation.Excel.Type;
import cn.bcs.common.core.domain.BaseDBEntity;
import cn.bcs.common.enums.CommonEnum;
import cn.bcs.common.enums.SysUserType;
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
    @DictConvert(dictType = "sys_user_type")
    @ApiModelProperty(value = "用户类型" + SysUserType.INFO)
    private String userType;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

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

    @ApiModelProperty("旧上级代理ID")
    private Long oldFromUserId;

    @ApiModelProperty("已确认佣金")
    private BigDecimal balance;

    @ApiModelProperty("上周待确认佣金")
    private BigDecimal waitInBalance;

    @ApiModelProperty("话费抽成金额")
    private BigDecimal callBalance;

    @ApiModelProperty("收款码链接")
    private String shoukuanUrl;

    @ApiModelProperty("连续未开单月份")
    private Long noApplyMonth;

    @ApiModelProperty("是否欠费" + CommonEnum.INFO)
    @DictConvert(dictType = "common")
    private Integer qianfei;

    @ApiModelProperty("下级所有话费总和包含自己发展的")
    private BigDecimal huafeiTeamTotal;

    @ApiModelProperty("我的费率")
    private BigDecimal huafeiTeamTotalRate;

    @ApiModelProperty("下级分成总和")
    private BigDecimal huafeiSubFenTotal;

    @ApiModelProperty("我的话费分成")
    private BigDecimal huafeiTeamFen;

}
