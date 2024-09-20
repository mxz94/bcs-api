package cn.bcs.web.selectData.domain.vo;

import cn.bcs.web.selectData.constants.SelectDataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: mxz
 * @time: 2023-05-17 14:10
 */
@Data
public class SelectDataQuery {
    private String name;
    private String status;
    @ApiModelProperty(value = "选择数据类型" + SelectDataType.INFO)
    private String type;
}
