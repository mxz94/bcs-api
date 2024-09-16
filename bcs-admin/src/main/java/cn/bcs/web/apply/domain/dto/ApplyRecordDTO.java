package cn.bcs.web.apply.domain.dto;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 套餐申请记录对象 b_apply_record
 *
 * @author mxz
 * @date 2024-09-14
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "套餐申请记录对象", description = "套餐申请记录对象")
public class ApplyRecordDTO {
private static final long serialVersionUID=1L;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    /** 身份证号 */
    @NotBlank(message = "身份证号不能为空")
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCard;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    /** 套餐id */
    @NotNull(message = "套餐id不能为空")
    @ApiModelProperty(value = "套餐id", required = true)
    private Long taocanId;

    /** 推荐人 */
    @ApiModelProperty(value = "推荐人")
    private Long fromUserId;

    ///** 微信openId */
    //@ApiModelProperty(value = "微信openId")
    //private Long openId;

}
