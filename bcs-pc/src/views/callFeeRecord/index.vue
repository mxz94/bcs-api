<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="nickName">
        <el-select v-model="queryParams.userId" filterable placeholder="请选择" clearable>
          <el-option
            v-for="item in options"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="月份" prop="month">
        <el-input
          v-model="queryParams.month"
          placeholder="请输入月份"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="callFeeRecordList" @selection-change="handleSelectionChange">
      <el-table-column label="话费总和" align="center" prop="sumCallFee" />
      <el-table-column label="分成比例" align="center" prop="rate" />
      <el-table-column label="当月话费分成" align="center" prop="fee" />
      <el-table-column label="代理" align="center" prop="nickName" />
      <el-table-column label="旧金额" align="center" prop="oldBalance" />
      <el-table-column label="新金额" align="center" prop="newBalance" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="月份" align="center" prop="month" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { listCallFeeRecord, getCallFeeRecord, delCallFeeRecord, addCallFeeRecord, updateCallFeeRecord } from "@/api/callFeeRecord/callFeeRecord";
import { getUserSelect } from "@/api/system/user";

export default {
  name: "CallFeeRecord",
  data() {
    return {
      options: [],
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
      // 话费分成记录表格数据
      callFeeRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        type: 2,
        pageNum: 1,
        pageSize: 10,
        userId: null,
        month: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getSelect();
  },
  methods: {
    getSelect() {
      getUserSelect().then(response => {
        this.options = response.data
      })
    },
    /** 查询话费分成记录列表 */
    getList() {
      this.loading = true;
      listCallFeeRecord(this.queryParams).then(response => {
        this.callFeeRecordList = response.rows;
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
        recordIds: null,
        sumCallFee: null,
        rate: null,
        fee: null,
        userId: null,
        oldBalance: null,
        newBalance: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        month: null
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
      this.title = "添加话费分成记录";
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCallFeeRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑话费分成记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCallFeeRecord(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCallFeeRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除话费分成记录编号为"' + ids + '"的数据项？').then(function() {
        return delCallFeeRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('callFeeRecord/callFeeRecord/export', {
        ...this.queryParams
      }, `callFeeRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
