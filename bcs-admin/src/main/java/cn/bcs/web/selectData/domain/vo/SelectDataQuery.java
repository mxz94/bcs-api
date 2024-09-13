package cn.bcs.web.selectData.domain.vo;

import lombok.Data;

/**
 * @description:
 * @author: mxz
 * @time: 2023-05-17 14:10
 */
@Data
public class SelectDataQuery {
    private String name;
    private String status;
    private String type;
    private Long tenantId;
}
