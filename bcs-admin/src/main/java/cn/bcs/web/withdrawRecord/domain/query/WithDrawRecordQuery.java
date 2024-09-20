package cn.bcs.web.withdrawRecord.domain.query;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.core.page.PageDomain;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@ApiModel(value = "用户记录查询", description = "用户记录查询")
public class WithDrawRecordQuery extends PageDomain {
    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotBlank(message = "状态，0 待审批 1 通过  2 驳回")
    @ApiModelProperty(value = "状态，0 待审批 1 通过  2 驳回", required = true)
    private String status;

    /** 提现类型  1 佣金  2 话费 */
    @ApiModelProperty(value = "提现类型（公众号必传）：" + WithdrawTypeEnum.INFO)
    private String type;


    @ApiModelProperty(value = "备注")
    private String remark;

}