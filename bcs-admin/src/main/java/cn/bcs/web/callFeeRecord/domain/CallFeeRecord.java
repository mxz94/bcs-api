package cn.bcs.web.callFeeRecord.domain;

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
    @ApiModelProperty(value = "${column.columnComment}")
    private Long id;

    /** 统计合计使用的apply_record_id */
    @ApiModelProperty(value = "统计合计使用的apply_record_id")
    private String recordIds;

    /** 当月所办卡总花费 */
    @ApiModelProperty(value = "当月所办卡总花费")
    private BigDecimal sumCallFee;

    /** 计算花费分成所用比例 */
    @ApiModelProperty(value = "计算花费分成所用比例")
    private BigDecimal rate;

    /** 当月话费分成 */
    @ApiModelProperty(value = "当月话费分成")
    private BigDecimal fee;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    /** 计算前总话费分成 */
    @ApiModelProperty(value = "计算前总话费分成")
    private BigDecimal oldBalance;

    /** 计算后总话费分成 */
    @ApiModelProperty(value = "计算后总话费分成")
    private BigDecimal newBalance;

    /** 月份 */
    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "类型： 话费分成， 团队奖")
    private String type;

}
