package cn.bcs.framework.dict.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhaoshuaixiang
 * @date 2024/4/15 9:40:46
 */
@Data
@Accessors(chain = true)
public class DictDataVO {

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    @ApiModelProperty(value = "样式")
    private String cssClass;
}
