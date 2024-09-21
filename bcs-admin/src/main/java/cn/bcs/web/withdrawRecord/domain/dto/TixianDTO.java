package cn.bcs.web.withdrawRecord.domain.dto;

import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TixianDTO {
    @NotNull(message = "提现类型不能为空")
    @ApiModelProperty(value = "提现类型："+ WithdrawTypeEnum.INFO, required = true)
    private String type;
    @NotNull(message = "提现金额不能为空")
    @ApiModelProperty(value = "提现金额", required = true)
    private BigDecimal amount;
}
