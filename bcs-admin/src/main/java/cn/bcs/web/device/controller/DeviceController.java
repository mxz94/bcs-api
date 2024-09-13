package cn.bcs.web.device.controller;

import java.util.Arrays;
import java.util.List;

import cn.bcs.web.device.domain.dto.DeviceActivationDTO;
import cn.bcs.web.device.domain.query.DeviceQuery;
import cn.bcs.web.device.domain.vo.DeviceInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.bcs.common.annotation.Log;
import cn.bcs.common.core.controller.BaseController;
import cn.bcs.common.core.domain.Result;
import cn.bcs.common.enums.BusinessType;
import cn.bcs.web.device.domain.Device;
import cn.bcs.web.device.service.DeviceService;
import cn.bcs.common.core.page.TableDataInfo;

/**
 * 设备Controller
 *
 * @author mxz
 * @date 2023-05-18
 */
@Api(tags = "设备管理")
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {
    @Autowired
    private DeviceService deviceService;

    /**
     * 查询设备列表
     */
    @ApiOperation(value = "查询设备列表")
    @GetMapping("/list")
    public TableDataInfo<List<DeviceInfo>> list(DeviceQuery query) {
        startPage();
        List<DeviceInfo> list = deviceService.selectList(query);
        return getDataTable(list);
    }

    /**
     * 获取设备详细信息
     */
    @ApiOperation(value = "获取设备详细信息")
    @GetMapping(value = "/info/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(deviceService.getById(id));
    }

    /**
     * 新增设备
     */
    @ApiOperation(value = "新增设备")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public Result<Device> add(@RequestBody Device device) {
        return deviceService.add(device);
    }

    /**
     * 修改设备
     */
    @ApiOperation(value = "修改设备")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public Result edit(@RequestBody Device device) {
        return deviceService.deviceEdit(device);
    }

    /**
     * 删除设备
     */
    @ApiOperation(value = "删除设备")
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(deviceService.removeByIds(Arrays.asList(ids)));
    }


    @ApiOperation(value = "激活设备")
    @PostMapping("/activation")
    public Result activation(@RequestBody DeviceActivationDTO dto)
    {
        deviceService.lambdaUpdate().eq(Device::getId,dto.getDeviceId()).set(Device::getActivationTime, dto.getActivationTime()).update();
        return Result.success();
    }
}
