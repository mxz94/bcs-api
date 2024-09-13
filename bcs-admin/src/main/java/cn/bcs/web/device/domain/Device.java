package cn.bcs.web.device.domain;

    import java.util.Date;
    import com.fasterxml.jackson.annotation.JsonFormat;
import cn.bcs.common.core.domain.BaseDBEntity;
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
 * 设备对象 b_device
 *
 * @author mxz
 * @date 2023-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "设备对象", description = "设备对象")
@TableName("b_device")
public class Device extends BaseDBEntity {
private static final long serialVersionUID=1L;

    /** 设备ID */
    @ApiModelProperty(value = "设备ID")
    private Long id;

    /** 设备名称 */
    @NotBlank(message = "设备名称不能为空")
    @ApiModelProperty(value = "设备名称", required = true)
    private String deviceName;

    /** 出厂编码 */
    @NotBlank(message = "出厂编码不能为空")
    @ApiModelProperty(value = "出厂编码", required = true)
    private String deviceNo;

    /** 设备编号 */
    @NotBlank(message = "设备编号不能为空")
    @ApiModelProperty(value = "设备编号", required = true)
    private String deviceCode;

    /** 设备类型  1 标准网络版 2 RFID 网络版 */
    @ApiModelProperty(value = "设备类型  1 标准网络版 2 RFID 网络版")
    private String deviceType;

    /** 省id */
    @ApiModelProperty(value = "省id")
    private String provinceId;

    /** 市id */
    @ApiModelProperty(value = "市id")
    private String cityId;

    /** 地址区id */
    @ApiModelProperty(value = "地址区id")
    private String areaId;

    /** 街道详细 */
    @ApiModelProperty(value = "街道详细")
    private String street;

    /** 区域id */
    @ApiModelProperty(value = "区域id")
    private Long fileAreaId;

    /** 全称地址 */
    @ApiModelProperty(value = "全称地址")
    private String address;

    /** 在线状态（1 在线  0 离线） */
    @ApiModelProperty(value = "在线状态（1 在线  0 离线）")
    private String onlineStatus;

    /** 设备状态（1 正常 2 到期 3 禁用 4 异常） */
    @ApiModelProperty(value = "设备状态（1 正常 2 到期 3 禁用 4 异常）")
    private String status;

    /** 设备激活时间 */
    @ApiModelProperty(value = "设备激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date activationTime;

    /** 设备到期时间 */
    @ApiModelProperty(value = "设备到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /** 柜门数量 */
    @ApiModelProperty(value = "柜门数量")
    private Long cabinetCount;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;


}
