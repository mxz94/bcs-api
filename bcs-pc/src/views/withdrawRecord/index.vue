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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="withdrawRecordList" @selection-change="handleSelectionChange">
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.withdraw_status" :value="scope.row.status"  :style="getTagStyle(scope.row.status)"/>
        </template>
      </el-table-column>
      <el-table-column label="提现金额" align="center" prop="amount" />
      <el-table-column label="税率" align="center" prop="rate" />
      <el-table-column label="转账金额" align="center" prop="realAmount" />
      <el-table-column label="旧余额" align="center" prop="oldBalance" />
      <el-table-column label="提现类型" align="center" prop="type_dictText" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="昵称" align="center" prop="nickName" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="收款码" align="center" key="shoukuanUrl" prop="shoukuanUrl" >
          <template slot-scope="scope">
            <image-preview :src="scope.row.shoukuanUrl" :width="40" :height="40" />
          </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleStatus(scope.row)"
            v-hasPermi="['withdrawRecord:withdrawRecord:edit']"
          >审核</el-button>
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

    <!-- 添加或编辑提现记录对话框 -->
    <el-dialog :title="title" :visible.sync="openStatus" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="备注" prop="remark">
          <el-select v-model="form.status" placeholder="请选择审核状态" clearable>
            <el-option
                v-for="dict in dict.type.withdraw_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
                :disabled="dict.value === '0'"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" type="textarea"  :autosize="{ minRows: 2, maxRows: 4}"  />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitStatus">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWithdrawRecord, getWithdrawRecord, delWithdrawRecord, addWithdrawRecord, updateWithdrawRecord, handleStatus } from "@/api/withdrawRecord/withdrawRecord";

import { getUserSelect } from "@/api/system/user";

export default {
  name: "WithdrawRecord",
  dicts: ['withdraw_status'],
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
      // 提现记录表格数据
      withdrawRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openStatus: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null,
        amount: null,
        rate: null,
        realAmount: null,
        oldBalance: null,
        newBalance: null,
        type: null,
        nickName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "审核记录状态，1 通过  2 驳回", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getSelect();
  },
  methods: {
    getTagStyle(status) {
      switch (status) {
        case "0" : // 未办理
          return { color: 'green' };
        case "1": // 办理通过
          return {  color: 'blue' };
        case "2": // 办理拒绝
          return {  color: 'red' };
        default:
          return {};
      }
    },
    handleStatus(row) {
      this.reset();
      this.form.id = row.id
      this.openStatus = true;
      this.title = "审核套餐";
    },
  submitStatus() {
    handleStatus(this.form).then(response => {
      this.$modal.msgSuccess("修改成功");
      this.openStatus = false;
      this.getList();
    });
  },
    /** 查询提现记录列表 */
    getSelect() {
      getUserSelect().then(response => {
        this.options = response.data
      })
    },
    getList() {
      this.loading = true;
      listWithdrawRecord(this.queryParams).then(response => {
        this.withdrawRecordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openStatus = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        status: null,
        amount: null,
        rate: null,
        realAmount: null,
        oldBalance: null,
        newBalance: null,
        type: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        nickName: null
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
      this.title = "添加提现记录";
    },
    /** 编辑按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdrawRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "审核提现记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWithdrawRecord(this.form).then(response => {
              this.$modal.msgSuccess("编辑成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWithdrawRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除提现记录编号为"' + ids + '"的数据项？').then(function() {
        return delWithdrawRecord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('withdrawRecord/withdrawRecord/export', {
        ...this.queryParams
      }, `withdrawRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
