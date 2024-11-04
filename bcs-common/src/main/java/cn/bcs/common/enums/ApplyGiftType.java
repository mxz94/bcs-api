
package cn.bcs.common.enums;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * @author m
 * @date 2022/4/11 17:00
 */
@Getter
@DictConfig(dictType = "apply_gift_type", dictName = "申请礼物类型")
public enum ApplyGiftType {
    /**
     * 申请礼物类型
     */
    GIFT_A("1", "手机"),
    GIFT_B("2", "酒")
    ;

    private final String code;
    private final String desc;

    public static final String INFO = " 01:礼物A，02:礼物B，03:礼物C，04:礼物D";

    ApplyGiftType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ApplyGiftType getByCode(String code) {
        if (code != null) {
            for (ApplyGiftType type : ApplyGiftType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}