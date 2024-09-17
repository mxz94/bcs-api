package cn.bcs.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    /**
     * 累加多个BigDecimal值。
     * 如果传入的参数为null，则将其视为0进行计算。
     *
     * @param numbers 可变参数列表，可以包含null值
     * @return 累加的结果
     */
    public static BigDecimal add(BigDecimal... numbers) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal number : numbers) {
            if (number == null) {
                number = BigDecimal.ZERO;
            }
            result = result.add(number);
        }
        return result;
    }

    public static int compare(BigDecimal a, BigDecimal b) {
        a = (a == null) ? BigDecimal.ZERO : a;
        b = (b == null) ? BigDecimal.ZERO : b;
        return a.compareTo(b);
    }

}
