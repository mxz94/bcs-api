package cn.bcs.web.tenant.mapper;

import cn.bcs.web.tenant.domain.SysTenant;
import cn.bcs.web.tenant.domain.query.TenantQuery;
import cn.bcs.web.tenant.domain.vo.TenantVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户Mapper接口
 *
 * @author ruoyi
 * @date 2023-05-08
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {
    /**
     * 查询租户列表
     *
     * @param query 租户查询对象
     * @return 租户集合
     */
    List<TenantVO> selectTenantList(@Param("query") TenantQuery query);
}
