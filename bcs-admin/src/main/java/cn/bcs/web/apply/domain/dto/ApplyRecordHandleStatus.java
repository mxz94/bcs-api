package cn.bcs.web.apply.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyRecordHandleStatus {
    private Long id;
    private String status;
    @ApiModelProperty(value ="操作类型 1 手机  2 酒")
    private String giftType;
    private String remark;
}
