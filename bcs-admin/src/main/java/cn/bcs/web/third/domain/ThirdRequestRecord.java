package cn.bcs.web.third.domain;

import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author m
 * @date 2022/5/26 11:23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "b_third_request_record")
public class ThirdRequestRecord extends BaseDBEntity {
    /**
     * 自增Id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 描述
     */
    private String description;
    /**
     * 发送参数
     */
    private String param;
    /**
     * 返回结果
     */
    private String result;
}
