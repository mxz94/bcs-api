package cn.bcs.web.callFeeRecord.constants;

import cn.bcs.common.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class HuafeiRateUtils {

    public static BigDecimal calculateTaxAmount(BigDecimal amount, BigDecimal rate) {
        // 计算金额，乘以税率并使用指定的舍入方式
        return BigDecimalUtils.multiplyPercentage(amount, rate);
    }

    public static BigDecimal calculateTaxRate(BigDecimal amount) {
        BigDecimal rate;
        if (amount.compareTo(BigDecimal.valueOf(125000)) >= 0) {
            rate = BigDecimal.valueOf(34);  // 34%
        } else if (amount.compareTo(BigDecimal.valueOf(80000)) >= 0) {
            rate = BigDecimal.valueOf(31);  // 31%
        } else if (amount.compareTo(BigDecimal.valueOf(42000)) >= 0) {
            rate = BigDecimal.valueOf(28);  // 28%
        } else if (amount.compareTo(BigDecimal.valueOf(24000)) >= 0) {
            rate = BigDecimal.valueOf(25);  // 25%
        } else if (amount.compareTo(BigDecimal.valueOf(12000)) >= 0) {
            rate = BigDecimal.valueOf(22);  // 22%
        } else if (amount.compareTo(BigDecimal.valueOf(6000)) >= 0) {
            rate = BigDecimal.valueOf(19);  // 19%
        } else if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            rate = BigDecimal.valueOf(16);  // 16%
        } else {
            throw new IllegalArgumentException("无效金额");
        }
        // 计算金额，乘以税率并使用指定的舍入方式
        return rate;
    }

}
