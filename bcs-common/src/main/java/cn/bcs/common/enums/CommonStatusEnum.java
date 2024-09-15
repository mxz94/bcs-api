package cn.bcs.common.enums;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * @author m
 * @date 2022/4/11 17:00
 */
@Getter
@DictConfig(dictType = "common_status", dictName = "通用状态")
public enum CommonStatusEnum {
    /**
     * 通用状态 1正常 0禁用
     */
    NORMAL("0", "正常"),
    DISABLED("1", "禁用"),
    ;

    private final String code;
    private final String desc;

    public static final String INFO = " 0:正常，1:禁用";

    CommonStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommonStatusEnum getByCode(String code) {
        if (code != null) {
            for (CommonStatusEnum status : CommonStatusEnum.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
