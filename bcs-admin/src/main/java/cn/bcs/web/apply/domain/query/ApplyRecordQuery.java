package cn.bcs.web.apply.domain.query;

import cn.bcs.common.annotation.DictConvert;
import cn.bcs.common.core.domain.BaseDBEntity;
import cn.bcs.web.apply.constants.ApplyStatus;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 套餐申请记录对象 b_apply_record
 *
 * @author mxz
 * @date 2024-09-14
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "套餐申请记录对象", description = "套餐申请记录对象")
public class ApplyRecordQuery {

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /** 套餐id */
    @ApiModelProperty(value = "taocanName")
    private String taocanName;

    /** 申请记录状态，0 未办理 1 办理通过  2 办理拒绝 */
    @ApiModelProperty(value = "申请记录状态，" + ApplyStatus.INFO)
    private String status;

    /** 推荐人 */
    @ApiModelProperty(value = "推荐人")
    private Long fromUserId;

    /** 微信openId */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    private String remark;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

}
