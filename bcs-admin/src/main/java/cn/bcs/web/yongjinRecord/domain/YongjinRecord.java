package cn.bcs.web.yongjinRecord.domain;

    import java.math.BigDecimal;
import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
    import lombok.Builder;
    import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 佣金分成记录对象 b_yongjin_record
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "佣金分成记录对象", description = "佣金分成记录对象")
@TableName("b_yongjin_record")
public class YongjinRecord extends BaseDBEntity {
private static final long serialVersionUID=1L;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private Long id;

    /** 办理记录id */
    @NotNull(message = "办理记录id不能为空")
    @ApiModelProperty(value = "办理记录id", required = true)
    private Long recordId;

    @ApiModelProperty(value = "办理用户")
    private String recordNickName;

    /** 佣金 */
    @ApiModelProperty(value = "佣金")
    private BigDecimal fee;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 计算前总话费分成 */
    @ApiModelProperty(value = "计算前总话费分成")
    private BigDecimal oldBalance;

    /** 计算后总话费分成 */
    @ApiModelProperty(value = "计算后总话费分成")
    private BigDecimal newBalance;

}
