package cn.bcs.web.device.mapper;

import cn.bcs.web.device.domain.Device;
import cn.bcs.web.device.domain.query.DeviceQuery;
import cn.bcs.web.device.domain.vo.DeviceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备Mapper接口
 *
 * @author mxz
 * @date 2023-05-18
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

    List<DeviceInfo> selectList(@Param("query") DeviceQuery query);
}
