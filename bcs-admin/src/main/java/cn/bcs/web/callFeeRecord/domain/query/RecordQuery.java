package cn.bcs.web.callFeeRecord.domain.query;

import cn.bcs.common.core.page.PageDomain;
import cn.bcs.web.withdrawRecord.constants.WithdrawTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 佣金分成记录对象 b_yongjin_record
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Data
@ApiModel(value = "用户记录查询", description = "用户记录查询")
public class RecordQuery extends PageDomain {
    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    private String month;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：" + WithdrawTypeEnum.INFO, required = true)
    private String type;

    @ApiModelProperty(value = "记录id")
    private Long recordId;

}
