package cn.bcs.web.apply.domain;

import java.math.BigDecimal;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 套餐申请记录对象 b_apply_record
 *
 * @author mxz
 * @date 2024-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "套餐申请记录对象", description = "套餐申请记录对象")
@TableName("b_apply_record")
public class ApplyRecord extends BaseDBEntity {
private static final long serialVersionUID=1L;

    /**  */
    @ApiModelProperty(value = "")
    private Long id;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    /** 身份证号 */
    @NotBlank(message = "身份证号不能为空")
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCard;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    /** 套餐id */
    @NotNull(message = "套餐id不能为空")
    @ApiModelProperty(value = "套餐id", required = true)
    private Long taocanId;

    /** 套餐名称 */
    @NotBlank(message = "套餐名称不能为空")
    @ApiModelProperty(value = "套餐名称", required = true)
    private String taocanName;

    /** 套餐价格 */
    @NotNull(message = "套餐价格不能为空")
    @ApiModelProperty(value = "套餐价格", required = true)
    private BigDecimal taocanValue;

    /** 申请记录状态，0 未办理 1 办理通过  2 办理拒绝 */
    @NotNull(message = "申请记录状态，0 未办理 1 办理通过  2 办理拒绝不能为空")
    @ApiModelProperty(value = "申请记录状态，0 未办理 1 办理通过  2 办理拒绝", required = true)
    @DictConvert(dictType = "apply_status")
    private String status;

    /** 推荐人 */
    @ApiModelProperty(value = "推荐人")
    private Long fromUserId;

    /** 微信openId */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    private String remark;

}
