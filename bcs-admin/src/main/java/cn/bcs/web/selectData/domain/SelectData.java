package cn.bcs.web.selectData.domain;

import cn.bcs.common.core.domain.BaseDBEntity;
import cn.bcs.web.selectData.constants.SelectDataType;
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
 * 选择内容对象 b_select_data
 *
 * @author ruoyi
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "选择内容对象", description = "选择内容对象")
@TableName("b_select_data")
public class SelectData extends BaseDBEntity {
private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "备注")
    private String remark;

    /** 类别 */
    @ApiModelProperty(value = "类别" + SelectDataType.INFO)
    private String type;

    /** 0 启用 1 停用 */
    @ApiModelProperty(value = "0 启用 1 停用")
    private String status;

    /** 0 未删除  1 已删除 */
    @ApiModelProperty(value = "0 未删除  1 已删除")
    private String delFlag;

    /** $column.columnComment */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "排序")
    private Integer sortNum;


}
