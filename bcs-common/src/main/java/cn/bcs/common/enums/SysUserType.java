package cn.bcs.common.enums;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 用户类型
 *
 * @author zhaoshuaixiang
 * @date 2023/5/8 18:21
 */
@Getter
@DictConfig(dictType = "sys_user_type", dictName = "用户类型")
public enum SysUserType {
    /**
     * 是
     */
    ADMIN("00", "管理员"),
    SUB_ACCOUNT("01", "子账号"),
    ;

    public static final String INFO = " 00:管理员，01:子账号";
    private final String code;
    private final String desc;

    SysUserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SysUserType getByCode(String code) {
        if (code != null) {
            for (SysUserType status : SysUserType.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
