package cn.bcs.web.wechat.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (WxSubTemplate)表实体类
 *
 * @author mxz
 * @since 2024-01-17 15:17:57
 */
@Data
@Accessors(chain = true)
@TableName(value = "wx_sub_template")
public class WxSubTemplate {

    private Integer id;

    private String type;

    private Integer subType;

    private String templateId;

    private Integer tenantId;

    private String remark;
}

