<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="租户名称" prop="tenantName">
        <el-input v-model="queryParams.tenantName" placeholder="请输入租户名称" clearable style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <div class="top-right-btn">
        <el-col :span="1.5">
          <el-button type="primary" @click="handleAdd" v-hasPermi="['system:tenant:add']">添加</el-button>
        </el-col>
      </div>
    </el-row>

    <el-table v-loading="loading" :data="tenantList" border>
      <el-table-column label="租户名称" align="center" prop="tenantName" />
      <el-table-column label="用户名" align="center" prop="userName" />
      <el-table-column label="用户数量限制" align="center" prop="userNumLimit" />
      <el-table-column label="状态" align="center" key="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" @click="handleUpdate(scope.row)" v-hasPermi="['system:tenant:edit']">编辑
          </el-button>
          <el-button type="text" @click="handleStatusChange(scope.row)" v-hasPermi="['system:tenant:edit']">{{
            scope.row.status === '1' ? '启用' : '禁用' }}
          </el-button>
          <el-button type="text" @click="handleDelete(scope.row)" v-hasPermi="['system:tenant:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或编辑租户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户名称" />
        </el-form-item>
        <el-form-item label="租户角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择租户角色">
            <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"
              :disabled="item.status == 1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名" :maxlength="20" show-word-limit>
            <span slot="suffix">初始密码：123456</span>
          </el-input>
        </el-form-item>
        <el-form-item v-if="form.tenantId == undefined" label="登录密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" type="password" :maxlength="20" show-password />
        </el-form-item>
        <el-form-item label="用户数量限制" prop="userNumLimit">
          <el-input v-model="form.userNumLimit" placeholder="请输入用户数量限制" :maxlength="20" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addTenant, changeUserStatus, delTenant, getTenant, listTenant, updateTenant } from "@/api/system/tenant";

export default {
  name: "Tenant",
  dicts: ['sys_common_status'],
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
      // 租户表格数据
      tenantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 角色选项
      roleOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tenantName: null,
        userName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantName: [{ required: true, message: "请输入租户名称", trigger: "blur" }],
        userName: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        roleId: [{ required: true, message: "请选择租户角色", trigger: "change" }],
        userNumLimit: [{ pattern: /^[1-9]\d*$/, message: '仅可输入正整数', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询租户列表 */
    getList() {
      this.loading = true;
      listTenant(this.queryParams).then(response => {
        this.tenantList = response.rows;
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
        tenantId: null,
        tenantName: null,
        status: null,
        delFlag: null,
        userNumLimit: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
    /** 添加按钮操作 */
    handleAdd() {
      this.reset();
      getTenant().then(response => {
        this.roleOptions = response.data.roles;
        this.open = true;
        this.title = "添加租户";
      });
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const tenantId = row.tenantId || this.ids
      getTenant(tenantId).then(response => {
        this.roleOptions = response.data.roles;
        this.form = response.data;
        this.open = true;
        this.title = "编辑租户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.tenantId != null) {
            updateTenant(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTenant(this.form).then(response => {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 修改租户状态 */
    handleStatusChange(row) {
      let text = row.status === "1" ? "启用" : "禁用";
      this.$modal.confirm('确认要"' + text + '"吗？').then(function () {
        return changeUserStatus(row.tenantId, row.status === "1" ? "0" : "1");
      }).then((response) => {
        this.getList();
        this.$modal.msgSuccess(response.msg);
      }).catch(() => {
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const tenantIds = row.tenantId || this.ids;
      this.$modal.confirm('是否确认删除租户编号为"' + tenantIds + '"的数据项？').then(function () {
        return delTenant(tenantIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tenant/export', {
        ...this.queryParams
      }, `tenant_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
