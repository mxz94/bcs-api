package cn.bcs.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class BigDecimalUtils {
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public static boolean isNull(BigDecimal bigDecimal) {
        return bigDecimal == null;
    }

    public static boolean isNullOrEqZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(ZERO) == 0;
    }

    public static boolean isNullOrLessZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(ZERO) < 0;
    }

    public static boolean isNullOrLessOrEqZero(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(ZERO) <= 0;
    }

    public static  boolean isGreaterThanZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return false;
        }
        return bigDecimal.compareTo(ZERO) > 0;
    }

    public static boolean isGreaterThanOrEqualToZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return false;
        }
        return bigDecimal.compareTo(ZERO) >= 0;
    }

    public static boolean isGreaterThanOrEqual(BigDecimal first, BigDecimal after) {
        return first.compareTo(after) >= 0;
    }

    public static boolean isLessThan(BigDecimal first, BigDecimal after) {
        return first.compareTo(after) < 0;
    }

    /**
     * 将字符串转换成BigDecimal对象
     */
    public static BigDecimal stringToBigDecimal(String stringBigdecimal) {
        if (StringUtils.isBlank(stringBigdecimal)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(stringBigdecimal).setScale(2, 4);
        } catch (Exception e) {

        }
        return BigDecimal.ZERO;
    }

    /**
     * 将null转换为0
     */
    public static BigDecimal bigDecimalNullToZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        } else {
            return bigDecimal;
        }
    }

    public static BigDecimal multiply(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return null;
        }
        return x.multiply(y);
    }

    public static BigDecimal multiplyPercentage(BigDecimal x, BigDecimal y, int roundingMode) {
        if (x == null || y == null) {
            return null;
        }
        return x.multiply(y).divide(HUNDRED, 2, roundingMode);
    }

    /**
     * 除法计算(result = x ÷ y)
     *
     * @param mom 被除数（可为null）
     * @param son 除数（可为null）
     * @return 商 （可为null,四舍五入，默认保留20位小数）
     */
    public static BigDecimal divide(BigDecimal mom, BigDecimal son) {
        if (mom == null || son == null || son.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        // 结果为0.000..时，不用科学计数法展示
        return mom.divide(son, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal subtract(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return null;
        }
        return x.subtract(y);
    }

    public static BigDecimal add(BigDecimal x, BigDecimal y) {
        if (x == null) {
            return y;
        }
        if (y == null) {
            return x;
        }
        return x.add(y);
    }
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
