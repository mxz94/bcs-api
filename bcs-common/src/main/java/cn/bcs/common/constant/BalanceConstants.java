package cn.bcs.common.constant;

import java.math.BigDecimal;

public interface BalanceConstants {

    public static final String WAIT_IN_BALANCE= "wait_in_balance";

    public static final String CALL_BALANCE= "call_balance";

    public static final String BALANCE = "balance";

    public static final BigDecimal GONGXIAN_125000 = BigDecimal.valueOf(125000);

    public static final BigDecimal HEHUO_350 = BigDecimal.valueOf(350);

    public static final BigDecimal HEHUO_35 = BigDecimal.valueOf(35);

    public static final BigDecimal DAILI_200 = BigDecimal.valueOf(200);

    public static final BigDecimal DAILI_150 = BigDecimal.valueOf(150);




    // 提现个人所得税 8.6%
    public static final BigDecimal SUODESHUI = BigDecimal.valueOf(8.6);
    // 最小提现佣金金额
    public static final BigDecimal MIN_YONGJIN = BigDecimal.valueOf(20);

    public static final BigDecimal MIN_HUAFEI = BigDecimal.valueOf(60);

    public static final Long noApplyMonth = 2L;

    public static final Integer TIXIAN_MONTH = 28;

    public static final BigDecimal MAX_RATE_38 = BigDecimal.valueOf(38);

    public static final BigDecimal GONGXIAN_5000 = BigDecimal.valueOf(5000);
}
