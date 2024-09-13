package cn.bcs.web.device.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @description:
 * @author: mxz
 * @time: 2023-05-18 17:13
 */
@Data
public class DeviceQuery {
    private Long tenantId;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "出厂编码")
    private String deviceNo;
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;
    @ApiModelProperty(value = "设备类型  1 标准网络版 2 RFID 网络版")
    private String deviceType;
    @ApiModelProperty(value = "区域id")
    private Long fileAreaId;
    @ApiModelProperty(value = "全称地址")
    private String address;
    @ApiModelProperty(value = "在线状态（1 在线  0 离线）")
    private String onlineStatus;
    @ApiModelProperty(value = "设备状态（1 正常 2 到期 3 禁用 4 异常）")
    private String deviceStatus;
    @ApiModelProperty(value = "设备激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date activationBeginTime;

    @ApiModelProperty(value = "设备激活结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date activationEndTime;

    @ApiModelProperty(value = "过期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireBeginTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireEndTime;

}
