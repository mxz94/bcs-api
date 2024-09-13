<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出厂编码" prop="deviceNo">
        <el-input
          v-model="queryParams.deviceNo"
          placeholder="请输入出厂编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备类型  1 标准网络版 2 RFID 网络版" prop="deviceType">
        <el-select v-model="queryParams.deviceType" placeholder="请选择设备类型  1 标准网络版 2 RFID 网络版" clearable>
          <el-option
            v-for="dict in dict.type.device_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="省id" prop="provinceId">
        <el-input
          v-model="queryParams.provinceId"
          placeholder="请输入省id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="市id" prop="cityId">
        <el-input
          v-model="queryParams.cityId"
          placeholder="请输入市id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址区id" prop="areaId">
        <el-input
          v-model="queryParams.areaId"
          placeholder="请输入地址区id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="街道详细" prop="street">
        <el-input
          v-model="queryParams.street"
          placeholder="请输入街道详细"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域id" prop="fileAreaId">
        <el-input
          v-model="queryParams.fileAreaId"
          placeholder="请输入区域id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="全称地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入全称地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="在线状态" prop="onlineStatus">
        <el-select v-model="queryParams.onlineStatus" placeholder="请选择在线状态" clearable>
          <el-option
            v-for="dict in dict.type.device_online_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择设备状态" clearable>
          <el-option
            v-for="dict in dict.type.device_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备激活时间" prop="activationTime">
        <el-date-picker clearable
          v-model="queryParams.activationTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择设备激活时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="设备到期时间" prop="expireTime">
        <el-date-picker clearable
          v-model="queryParams.expireTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择设备到期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="柜门数量" prop="cabinetCount">
        <el-input
          v-model="queryParams.cabinetCount"
          placeholder="请输入柜门数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="租户id" prop="tenantId">
        <el-input
          v-model="queryParams.tenantId"
          placeholder="请输入租户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" material @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" material @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          material
          @click="handleAdd"
          v-hasPermi="['device:add']"
        >添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          material
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['device:edit']"
        >编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          material
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['device:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          material
          @click="handleExport"
          v-hasPermi="['device:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备ID" align="center" prop="id" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="出厂编码" align="center" prop="deviceNo" />
      <el-table-column label="设备编号" align="center" prop="deviceCode" />
      <el-table-column label="设备类型" align="center" prop="deviceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.device_type" :value="scope.row.deviceType"/>
        </template>
      </el-table-column>
      <el-table-column label="省id" align="center" prop="provinceId" />
      <el-table-column label="市id" align="center" prop="cityId" />
      <el-table-column label="地址区id" align="center" prop="areaId" />
      <el-table-column label="街道详细" align="center" prop="street" />
      <el-table-column label="区域id" align="center" prop="fileAreaId" />
      <el-table-column label="全称地址" align="center" prop="address" />
      <el-table-column label="在线状态" align="center" prop="onlineStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.device_online_status" :value="scope.row.onlineStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="设备状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.device_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="设备激活时间" align="center" prop="activationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.activationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备到期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="柜门数量" align="center" prop="cabinetCount" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="租户id" align="center" prop="tenantId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            material
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['device:edit']"
          >编辑</el-button>
          <el-button
            material
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或编辑设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="出厂编码" prop="deviceNo">
          <el-input v-model="form.deviceNo" placeholder="请输入出厂编码" />
        </el-form-item>
        <el-form-item label="设备编号" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="设备类型  1 标准网络版 2 RFID 网络版" prop="deviceType">
          <el-select v-model="form.deviceType" placeholder="请选择设备类型  1 标准网络版 2 RFID 网络版">
            <el-option
              v-for="dict in dict.type.device_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="省id" prop="provinceId">
          <el-input v-model="form.provinceId" placeholder="请输入省id" />
        </el-form-item>
        <el-form-item label="市id" prop="cityId">
          <el-input v-model="form.cityId" placeholder="请输入市id" />
        </el-form-item>
        <el-form-item label="地址区id" prop="areaId">
          <el-input v-model="form.areaId" placeholder="请输入地址区id" />
        </el-form-item>
        <el-form-item label="街道详细" prop="street">
          <el-input v-model="form.street" placeholder="请输入街道详细" />
        </el-form-item>
        <el-form-item label="区域id" prop="fileAreaId">
          <el-input v-model="form.fileAreaId" placeholder="请输入区域id" />
        </el-form-item>
        <el-form-item label="全称地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入全称地址" />
        </el-form-item>
        <el-form-item label="在线状态" prop="onlineStatus">
          <el-radio-group v-model="form.onlineStatus">
            <el-radio
              v-for="dict in dict.type.device_online_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设备状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.device_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设备激活时间" prop="activationTime">
          <el-date-picker clearable
            v-model="form.activationTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择设备激活时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="设备到期时间" prop="expireTime">
          <el-date-picker clearable
            v-model="form.expireTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择设备到期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="柜门数量" prop="cabinetCount">
          <el-input v-model="form.cabinetCount" placeholder="请输入柜门数量" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="租户id" prop="tenantId">
          <el-input v-model="form.tenantId" placeholder="请输入租户id" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDevice, getDevice, delDevice, addDevice, updateDevice } from "@/api/device/device";

export default {
  name: "Device",
  dicts: ['device_status', 'device_type', 'device_online_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 设备表格数据
      deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceName: null,
        deviceNo: null,
        deviceCode: null,
        deviceType: null,
        provinceId: null,
        cityId: null,
        areaId: null,
        street: null,
        fileAreaId: null,
        address: null,
        onlineStatus: null,
        status: null,
        activationTime: null,
        expireTime: null,
        cabinetCount: null,
        tenantId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        deviceNo: [
          { required: true, message: "出厂编码不能为空", trigger: "blur" }
        ],
        deviceCode: [
          { required: true, message: "设备编号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        deviceName: null,
        deviceNo: null,
        deviceCode: null,
        deviceType: null,
        provinceId: null,
        cityId: null,
        areaId: null,
        street: null,
        fileAreaId: null,
        address: null,
        onlineStatus: null,
        status: null,
        activationTime: null,
        expireTime: null,
        cabinetCount: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null,
        tenantId: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 添加按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备";
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDevice(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑设备";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDevice(this.form).then(response => {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除设备编号为"' + ids + '"的数据项？').then(function() {
        return delDevice(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('device/device/export', {
        ...this.queryParams
      }, `device_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
