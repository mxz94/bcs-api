package cn.bcs.web.withdrawRecord.constants;

import cn.bcs.common.annotation.DictConfig;
import lombok.Getter;

/**
 * @author m
 * @date 2022/4/11 17:00
 */
@Getter
@DictConfig(dictType = "withdraw_type", dictName = "提现类型")
public enum WithdrawTypeEnum {
    YONGJIN("1", "佣金"),
    /**
     * 是
     */
    HUAFEIFENCHENG("2", "话费分成"),
    /**
     * 否
     */
    TEAMBUILD("3", "团队奖金"),
    ;

    private final String code;
    private final String desc;

    public static final String INFO = "1 佣金 2 话费分成 3 团队奖金";

    WithdrawTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WithdrawTypeEnum getByCode(String code) {
        if (code != null) {
            for (WithdrawTypeEnum status : WithdrawTypeEnum.values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }
}
