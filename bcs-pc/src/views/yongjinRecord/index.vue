<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" >
      <el-form-item label="办理记录id" prop="recordId" label-width="140">
        <el-input
          v-model="queryParams.recordId"
          placeholder="请输入办理记录id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="yongjinRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="办理记录id" align="center" prop="recordId" />
      <el-table-column label="佣金" align="center" prop="fee" />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="计算前总话费分成" align="center" prop="oldBalance" />
      <el-table-column label="计算后总话费分成" align="center" prop="newBalance" />
      <el-table-column label="备注" align="center" prop="remark" />
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['yongjinRecord:yongjinRecord:edit']"-->
<!--          >编辑</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['yongjinRecord:yongjinRecord:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
import { listYongjinRecord, getYongjinRecord, delYongjinRecord, addYongjinRecord, updateYongjinRecord } from "@/api/yongjinRecord/yongjinRecord";
import {getUserSelect} from "@/api/system/user";

export default {
  name: "YongjinRecord",
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
      // 佣金分成记录表格数据
      yongjinRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        recordId: null,
        fee: null,
        userId: null,
        oldBalance: null,
        newBalance: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        recordId: [
          { required: true, message: "办理记录id不能为空", trigger: "blur" }
        ],
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
    /** 查询佣金分成记录列表 */
    getList() {
      this.loading = true;
      listYongjinRecord(this.queryParams).then(response => {
        this.yongjinRecordList = response.rows;
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
        recordId: null,
        fee: null,
        userId: null,
        oldBalance: null,
        newBalance: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加佣金分成记录";
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getYongjinRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑佣金分成记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateYongjinRecord(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addYongjinRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除佣金分成记录编号为"' + ids + '"的数据项？').then(function() {
        return delYongjinRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('yongjinRecord/yongjinRecord/export', {
        ...this.queryParams
      }, `yongjinRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
