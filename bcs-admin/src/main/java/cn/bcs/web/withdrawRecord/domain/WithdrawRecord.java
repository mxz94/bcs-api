package cn.bcs.web.withdrawRecord.domain;

    import java.math.BigDecimal;
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
    @NotBlank(message = "申请记录状态，0 待审批 1 通过  2 拒绝不能为空")
    @ApiModelProperty(value = "申请记录状态，0 待审批 1 通过  2 拒绝", required = true)
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

    /** 新余额 */
    @ApiModelProperty(value = "新余额")
    private BigDecimal newBalance;

    /** 提现类型  1 佣金  2 话费 */
    @ApiModelProperty(value = "提现类型  1 佣金  2 话费")
    private String type;

    /** 昵称 */
    @ApiModelProperty(value = "昵称")
    private String nickName;


}
