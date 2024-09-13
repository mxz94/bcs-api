package cn.bcs.web.device.service;

import java.util.List;

import cn.bcs.common.core.domain.Result;
import cn.bcs.common.utils.SecurityUtils;
import cn.bcs.common.utils.StringUtils;
import cn.bcs.web.device.domain.Device;
import cn.bcs.web.device.domain.query.DeviceQuery;
import cn.bcs.web.device.domain.vo.DeviceInfo;
import cn.bcs.web.device.mapper.DeviceMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 设备Service业务层处理
 *
 * @author mxz
 * @date 2023-05-18
 */
@Service
public class  DeviceService extends ServiceImpl<DeviceMapper, Device> {

    public Result<Device> add(Device device) {
        int count = this.count(new LambdaQueryWrapper<Device>().eq(Device::getDeviceNo, device.getDeviceNo()));
        if (count > 0) {
            return Result.error("设备编码已存在");
        }
        if (StringUtils.isEmpty(device.getDeviceName())) {
            device.setDeviceName("只能档案柜");
        }
        boolean save = this.save(device);
        // 生成默认设备配置
        return save ? Result.success(device) : Result.error();
    }

    /**
     * 修改设备
     *
     * @return Result
     */
    public Result<Device> deviceEdit(Device device) {
        int count = this.count(new LambdaQueryWrapper<Device>().eq(Device::getDeviceNo, device.getDeviceNo()).ne(Device::getId, device.getId()));
        if (count > 0) {
            return Result.error("设备编码已存在");
        }
        //aliIotSupport.sendToDevice(device.getDeviceNo(), PubCommandType.DEVICE_INFO_CHANGE);
        boolean update = this.updateById(device);
        return update ? Result.success(device) : Result.error();
    }

    public List<DeviceInfo> selectList(DeviceQuery query) {
        if (!SecurityUtils.isAdmin()) {
            query.setTenantId(SecurityUtils.getTenantId());
        }
        List<DeviceInfo> deviceInfos = this.getBaseMapper().selectList(query);
        return deviceInfos;
    }
}
