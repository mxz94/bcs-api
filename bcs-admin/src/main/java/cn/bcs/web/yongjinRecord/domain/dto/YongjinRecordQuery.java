package cn.bcs.web.yongjinRecord.domain.dto;

import cn.bcs.common.core.page.PageDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 佣金分成记录对象 b_yongjin_record
 *
 * @author ruoyi
 * @date 2024-09-17
 */
@Data
@ApiModel(value = "用户记录查询", description = "用户记录查询")
public class YongjinRecordQuery extends PageDomain {
    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

}
