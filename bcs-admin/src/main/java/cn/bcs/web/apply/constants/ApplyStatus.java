package cn.bcs.web.apply.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * 申请记录状态
 * 0 未办理, 1 办理通过, 2 办理拒绝
 * 
 * @author 
 * @date 2024/9/14
 */
@Getter
@DictConfig(dictType = "apply_status", dictName = "申请记录状态")
public enum ApplyStatus {
    PENDING("0", "未办理"),
    APPROVED("1", "通过"),
    REJECTED("2", "拒绝"),
    ZUOFEI("3", "通过(无分成)")
    ;

    public static final String INFO = "0:未办理，1:通过，2:拒绝，3:通过(无分成)";
    private final String code;
    private final String desc;

    ApplyStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ApplyStatus getByCode(String code) {
        if (code != null) {
            for (ApplyStatus status : ApplyStatus.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
