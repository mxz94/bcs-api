package cn.bcs.web.apply.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyRecordHandleStatus {
    private Long id;
    private String status;
    private String remark;
}
