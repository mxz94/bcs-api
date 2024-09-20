package cn.bcs.web.withdrawRecord.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * @author m
 * @date 2022/4/11 17:00
 */
@Getter
@DictConfig(dictType = "withdraw_status", dictName = "提现状态")
public enum WithdrawStatusEnum {
    PEDDING("0", "待处理"),
    /**
     * 是
     */
    PASS("1", "通过"),
    /**
     * 否
     */
    REJECT("2", "驳回"),
    ;

    private final String code;
    private final String desc;

    public static final String INFO = "0 待处理 1 通过  2 驳回";

    WithdrawStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WithdrawStatusEnum getByCode(String code) {
        if (code != null) {
            for (WithdrawStatusEnum status : WithdrawStatusEnum.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
