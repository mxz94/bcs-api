package cn.bcs.web.callFeeRecord.domain;

    import java.math.BigDecimal;
import cn.bcs.common.core.domain.BaseDBEntity;
    import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
    import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 话费分成记录对象 b_call_fee_record
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "话费分成记录对象", description = "话费分成记录对象")
@TableName("b_call_fee_record")
public class CallFeeRecord extends BaseDBEntity {
private static final long serialVersionUID=1L;

    /** $column.columnComment */
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "话费分成 ids,  佣金只有 id")
    private Long recordId;

    @ApiModelProperty("下级所有话费总和包含自己发展的")
    private BigDecimal huafeiTeamTotal;

    @ApiModelProperty("我的费率")
    private BigDecimal huafeiTeamTotalRate;

    @ApiModelProperty("下级分成总和")
    private BigDecimal huafeiSubFenTotal;

    /** 当月话费分成 */
    @ApiModelProperty(value = "金额")
    private BigDecimal fee;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 计算前总话费分成 */
    @ApiModelProperty(value = "旧余额")
    private BigDecimal oldBalance;

    /** 计算后总话费分成 */
    @ApiModelProperty(value = "新余额")
    private BigDecimal newBalance;

    /** 月份 */
    @ApiModelProperty(value = "月份(话费分成 和 团队奖金)")
    private String month;

    @ApiModelProperty(value = "类型：" + WithdrawTypeEnum.INFO)
    private String type;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
