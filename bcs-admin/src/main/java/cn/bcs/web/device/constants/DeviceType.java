package cn.bcs.web.device.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 启用禁用状态
 * @author zhaoshuaixiang
 * @date 2023/5/8 18:21
 */
@Getter
@DictConfig(dictType = "device_type", dictName = "设备类型")
public enum DeviceType {
    STRAND("1", "标准网络版"),
    RFID("2", "RFID 网络版");

    public static final String INFO = " 1 标准网络版 2 RFID 网络版";
    private final String code;
    private final String desc;

    DeviceType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DeviceType getByCode(String code) {
        if (code != null) {
            for (DeviceType status : DeviceType.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
