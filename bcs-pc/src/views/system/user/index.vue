
++


<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-row>
            <el-col>
              <el-form-item label="昵称" prop="nickName">
                <el-input v-model="queryParams.nickName" placeholder="请输入昵称" clearable style="width: 266px"
                  @keyup.enter.native="handleQuery" />
              </el-form-item>
              <el-form-item label="角色" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择角色" clearable style="width: 266px">
                  <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
                    :value="dict.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="用户名" prop="userName">
                <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable style="width: 266px"
                  @keyup.enter.native="handleQuery" />
              </el-form-item>
              <el-form-item label="手机号" prop="phonenumber" v-show="showSearch">
                <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号" clearable style="width: 266px"
                  @keyup.enter.native="handleQuery" />
              </el-form-item>
            </el-col>
            <el-col :span="24" :xs="24" v-show="showSearch">
              <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 266px">
                  <el-option v-for="dict in dict.type.sys_common_status" :key="dict.value" :label="dict.label"
                    :value="dict.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间">
                <el-date-picker v-model="dateRange" style="width: 460px" value-format="yyyy-MM-dd" type="daterange"
                  range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQuery">查询</el-button>
                <el-button @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <div class="top-right-btn">
            <el-col :span="1.5">
              <el-button type="primary" @click="handleAdd" v-hasPermi="['system:user:add']">添加
              </el-button>
            </el-col>
            <!-- <el-col :span="1.5">

            </el-col>
            <el-col :span="1.5">

            </el-col>
            <el-col :span="1.5">
            </el-col>
            <el-col :span="1.5">
            </el-col>
            <el-col :span="1.5">
            </el-col> -->
          </div>
          <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> -->
        </el-row>

        <el-table v-loading="loading" :data="userList" border style="width: 100%"
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" fixed />
          <el-table-column label="id" align="center" key="userId" prop="userId" />
          <el-table-column label="头像" align="center" key="avatar" prop="avatar">
            <template slot-scope="scope">
              <image-preview :url="scope.row.avatar" :width="40" :height="40" />
            </template>
          </el-table-column>
          <el-table-column label="昵称" align="center" key="nickName" prop="nickName" :show-overflow-tooltip="true" />
          <el-table-column label="用户名" align="center" key="userName" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column label="用户类型" align="center" key="userType_dictText" prop="userType_dictText" />
          <el-table-column label="手机号" align="center" key="phonenumber" prop="phonenumber" />
          <el-table-column label="佣金" align="center" key="balance" prop="balance" />
          <el-table-column label="上周待确认佣金" align="center" key="waitInBalance" prop="waitInBalance" />
          <el-table-column label="话费抽成" align="center" key="callBalance" prop="callBalance" />
          <el-table-column label="团队构建金" align="center" key="teamBuildBalance" prop="teamBuildBalance" />
          <el-table-column label="状态" align="center" key="status">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" width="160" prop="createTime"></el-table-column>
          <el-table-column label="推荐人" align="center" key="fromNickName" prop="fromNickName" />
          <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width" fixed="right">
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button v-if="scope.row.status === '1'" type="text" @click="handleStatusChange([scope.row.userId], '0')"
                v-hasPermi="['system:user:edit']">启用
              </el-button>
              <el-button v-else type="text" @click="handleStatusChange([scope.row.userId], '1')"
                v-hasPermi="['system:user:edit']">禁用
              </el-button>
              <el-button type="text" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" center :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="form.nickName" placeholder="请输入昵称" :maxlength="120" show-word-limit />
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名" :maxlength="120" show-word-limit />
        </el-form-item>
        <el-form-item v-if="form.userId == undefined" label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" type="password" :maxlength="120" show-password />
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择角色">
            <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"
              :disabled="item.status == 1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号" :maxlength="11" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body
      :close-on-click-modal="false">
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />
            是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addUser, changeUserStatus, delUser, getUser, listUser, resetUserPwd, updateUser } from "@/api/system/user";
import { getToken } from "@/utils/auth";

export default {
  name: "User",
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 20, message: '用户名长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        // nickName: [
        //   { required: true, message: "用户昵称不能为空", trigger: "blur" }
        // ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          { min: 5, max: 20, message: '密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        roleId: [{ required: true, message: "请选择角色", trigger: "change" }],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },
    // 用户状态修改
    handleStatusChange(userIds, status) {
      let text = status === "1" ? "禁用" : "启用";
      this.$modal.confirm('确认要' + text + '吗？', text).then(function () {
        return changeUserStatus(userIds, status);
      }).then((response) => {
        this.getList();
        this.$modal.msgSuccess(response.msg);
      }).catch(() => {
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
        userId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        status: "0",
        remark: undefined,
        roleId: undefined
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getUser().then(response => {
        this.roleOptions = response.data.roles;
        this.open = true;
        this.title = "添加用户";
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.roleOptions = response.data.roles;
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(userIds) {
      let value = this.initPassword;
      this.$modal.confirm('确认要重置密码为' + value + '吗？').then(function () {
        return resetUserPwd(userIds, value);
      }).then((response) => {
        this.getList();
        this.$modal.msgSuccess(response.msg);
      }).catch(() => {
      });
    },
    /** 分配角色操作 */
    handleAuthRole: function (row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess(response.msg);
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess(response.msg);
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户？').then(function () {
        return delUser(userIds);
      }).then((response) => {
        this.getList();
        this.$modal.msgSuccess(response.msg);
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {}, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>

<style scoped>
::v-deep .el-dialog__title {
  font-weight: bold;
  color: #333;
}
</style>
