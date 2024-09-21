package cn.bcs.web.withdrawRecord.domain;

    import java.math.BigDecimal;

    import cn.bcs.common.annotation.DictConvert;
    import cn.bcs.common.core.domain.BaseDBEntity;
    import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
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
 * 提现记录对象 b_withdraw_record
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "提现记录对象", description = "提现记录对象")
@TableName("b_withdraw_record")
public class WithdrawRecord extends BaseDBEntity {
private static final long serialVersionUID=1L;

    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 申请记录状态，0 待审批 1 通过  2 拒绝 */
    @NotBlank(message = "状态，0 待审批 1 通过  2 驳回")
    @ApiModelProperty(value = "状态，0 待审批 1 通过  2 驳回", required = true)
    @DictConvert(dictType = "withdraw_status")
    private String status;

    /** 提现金额 */
    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    /** 税率 */
    @ApiModelProperty(value = "税率")
    private BigDecimal rate;

    /** 转账金额 */
    @ApiModelProperty(value = "转账金额")
    private BigDecimal realAmount;

    /** 旧余额 */
    @ApiModelProperty(value = "旧余额")
    private BigDecimal oldBalance;


    /** 提现类型  1 佣金  2 话费 */
    @ApiModelProperty(value = "提现类型：" + WithdrawTypeEnum.INFO)
    @DictConvert(dictType = "withdraw_type")
    private String type;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}
