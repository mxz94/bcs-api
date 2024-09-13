package cn.bcs.web.device.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: mxz
 * @time: 2023-05-19 10:25
 */
@Data
public class DeviceActivationDTO {
    private String deviceId;

    /** 设备激活时间 */
    @ApiModelProperty(value = "设备激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date activationTime;
}
