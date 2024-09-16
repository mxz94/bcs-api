package cn.bcs.web.apply.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthCallFeeVO {

    private Long userId;

    private BigDecimal amount;

    private String recordIds;
}
