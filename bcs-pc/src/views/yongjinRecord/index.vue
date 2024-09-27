<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" >
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
      <el-form-item label="记录id" prop="queryParams.recordId">
        <el-input v-model="queryParams.recordId" placeholder="请输入" property="recordId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="yongjinRecordList" @selection-change="handleSelectionChange">
      <el-table-column label="办理id" align="center" prop="recordId" />
      <el-table-column label="用户" align="center" prop="recordNickName" />
      <el-table-column label="推荐人" align="center" prop="nickName" />
      <el-table-column label="佣金" align="center" prop="fee" />
      <el-table-column label="旧金额" align="center" prop="oldBalance" />
      <el-table-column label="新金额" align="center" prop="newBalance" />
      <el-table-column label="备注" align="center" prop="remark" />
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
import {getUserSelect} from "@/api/system/user";
import {listCallFeeRecord} from "@/api/callFeeRecord/callFeeRecord";

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
        type: '1',
        pageNum: 1,
        pageSize: 10,
        userId: null
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
      listCallFeeRecord(this.queryParams).then(response => {
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
    /** 导出按钮操作 */
    handleExport() {
      this.download('yongjinRecord/yongjinRecord/export', {
        ...this.queryParams
      }, `yongjinRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
