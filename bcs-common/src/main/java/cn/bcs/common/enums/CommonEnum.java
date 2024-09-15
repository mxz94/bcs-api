package cn.bcs.common.enums;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * @author m
 * @date 2022/4/11 17:00
 */
@Getter
@DictConfig(dictType = "common", dictName = "通用枚举")
public enum CommonEnum {
    /**
     * 是
     */
    YES(1, "是"),
    /**
     * 否
     */
    NO(0, "否"),
    ;

    private final Integer code;
    private final String desc;

    public static final String INFO = "1:是，0:否";

    CommonEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommonEnum getByCode(Integer code) {
        if (code != null) {
            for (CommonEnum status : CommonEnum.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
