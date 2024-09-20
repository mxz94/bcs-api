<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm"  :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="问题名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary"  @click="handleQuery">查询</el-button>
        <el-button   @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          @click="handleAdd"
          v-hasPermi="['system:area:edit']"
        >添加</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="selectDataList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column
        label="id"
        align="center"
        prop="id"
        >
      </el-table-column>
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="值" align="center" prop="value" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"  :class="[scope.row.status == '1' ? 'disable-color' : 'enable-color']"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:area:edit']"
          >编辑</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            type="text"
            @click="setStatus(scope.row, 1)"
            v-hasPermi="['system:area:status']"
          >禁用</el-button>
          <el-button
            v-else
            type="text"
            @click="setStatus(scope.row, 0)"
            v-hasPermi="['system:area:status']"
          >启用</el-button>
          <el-button
            type="text"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:area:remove']"
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

    <!-- 添加或编辑选择内容对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="问题名称:" prop="name">
          <el-input v-model="form.name" placeholder="请输入问题名称" />
        </el-form-item>
        <el-form-item label="值:" prop="value">
          <el-input   type="number" :rows="2" v-model="form.value" placeholder="请输入值" />
        </el-form-item>
        <el-form-item label="备注:" prop="remark">
          <el-input   type="textarea" :rows="2" v-model="form.remark" placeholder="请输入备注内容" />
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
import { listSelectData, getSelectData, delSelectData, addSelectData, updateSelectData } from "@/api/system/selectData";

export default {
  name: "SelectQuestion",
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
      // 选择内容表格数据
      selectDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: "question",
        status: null,
      },
      // 表单参数
      form: {
        type : "question",
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询选择内容列表 */
    getList() {
      this.loading = true;
      listSelectData(this.queryParams).then(response => {
        this.selectDataList = response.rows;
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
        name: null,
        remark: null,
        type: "question",
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
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
      this.title = "添加";
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSelectData(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑";
      });
    },
    /** 编辑按钮操作 */
    setStatus(row, status) {
      row.status = status
      let text = row.status === "0" ? "启用" : "禁用";
      this.$modal.confirm('确认要"' + text + '"吗？').then(function () {
        return updateSelectData(row);
      }).then((response) => {
        this.getList();
        this.$modal.msgSuccess(response.msg);
      }).catch(function () {
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSelectData(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSelectData(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除？').then(function() {
        return delSelectData(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/selectData/export', {
        ...this.queryParams
      }, `selectData_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
