package cn.bcs.common.enums;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 启用禁用状态
 * @author zhaoshuaixiang
 * @date 2023/5/8 18:21
 */
@Getter
@DictConfig(dictType = "sys_common_status", dictName = "启用禁用状态")
public enum SysCommonStatus {
    /**
     * 是
     */
    NORMAL("0", "正常"),
    DISABLE("1", "禁用"),
    ;

    public static final String INFO = " 0:正常，1:禁用";
    private final String code;
    private final String desc;

    SysCommonStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SysCommonStatus getByCode(String code) {
        if (code != null) {
            for (SysCommonStatus status : SysCommonStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
