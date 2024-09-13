package cn.bcs.web.device.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 启用禁用状态
 * @author zhaoshuaixiang
 * @date 2023/5/8 18:21
 */
@Getter
@DictConfig(dictType = "device_status", dictName = "设备状态")
public enum DeviceStatus {

    NORMAL("1", "正常"),
    EXPIRE("2", "到期"),
    DISABLED("3", "禁用"),
    ERROR("4", "异常");

    public static final String INFO = " 1 正常 2 到期 3 禁用 4 异常";
    private final String code;
    private final String desc;

    DeviceStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DeviceStatus getByCode(String code) {
        if (code != null) {
            for (DeviceStatus status : DeviceStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
