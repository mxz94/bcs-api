package cn.bcs.common.core.domain.entity;

import cn.bcs.common.core.domain.BaseDBEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典类型表 sys_dict_type
 *
 * @author ruoyi
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictType extends BaseDBEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @TableId(type = IdType.AUTO)
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;
}
