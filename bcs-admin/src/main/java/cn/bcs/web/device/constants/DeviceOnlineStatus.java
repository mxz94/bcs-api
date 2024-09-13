package cn.bcs.web.device.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * @author zhaoshuaixiang
 * @date 2022/4/27 15:36
 */
@Getter
@DictConfig(dictType = "device_online_status", dictName = "设备在线状态")
public enum DeviceOnlineStatus {
    /**
     * 设备在线状态 1在线、2离线
     */
    ONLINE(1, "在线"),
    OFFLINE(0, "离线");

    public static final String INFO = " 1在线、0离线";
    private Integer code;
    private String desc;

    DeviceOnlineStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DeviceOnlineStatus getByCode(Integer code) {
        if (code != null) {
            for (DeviceOnlineStatus status : DeviceOnlineStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
