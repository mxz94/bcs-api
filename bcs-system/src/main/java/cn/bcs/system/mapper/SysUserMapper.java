package cn.bcs.system.mapper;

import cn.bcs.common.core.domain.entity.SysUser;
import cn.bcs.system.domain.query.SysUserQuery;
import cn.bcs.system.domain.vo.SysUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据条件分页查询用户列表
     *
     * @param query    SysUserQuery
     * @param tenantId 租户ID
     * @return 用户信息集合信息
     */
    List<SysUserVO> selectUserList(@Param("query") SysUserQuery query, @Param("tenantId") Long tenantId);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(@Param("userName") String userName);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @param userId   用户ID
     * @return 结果
     */
    Integer checkUserNameUnique(@Param("userName") String userName, @Param("userId") Long userId);


    /**
     * 更新登录信息
     *
     * @param userId    用户ID
     * @param loginIp   登录IP地址
     * @param loginDate 登录时间
     */
    void updateLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp, @Param("loginDate") Date loginDate);
}
