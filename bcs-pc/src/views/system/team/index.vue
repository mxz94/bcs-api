<template>
  <div class="app-container">
    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="deptList"
      row-key="userId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="nickName" label="昵称" width="200"></el-table-column>
      <el-table-column prop="userId" label="id" width="60"></el-table-column>
      <el-table-column prop="userType_dictText" label="类型" width="60"></el-table-column>
      <el-table-column label="佣金" align="center" key="balance" prop="balance" />
      <el-table-column label="上周待确认佣金" align="center" key="waitInBalance" prop="waitInBalance" />
      <el-table-column label="话费分成" align="center" key="callBalance" prop="callBalance" />
      <el-table-column label="团队总和" align="center" prop="huafeiTeamTotal" width="200"/>
      <el-table-column label="分成比例" align="center" width="200">
        <template slot-scope="scope">
          {{ scope.row.huafeiTeamTotalRate }}%
        </template>
      </el-table-column>
      <el-table-column label="下级分成" align="center" prop="huafeiSubFenTotal" width="200" />
      <el-table-column label="我的分成" align="center" prop="huafeiTeamFen" width="200"/>
      <el-table-column label="创建时间" align="center" prop="createTime"class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {teamTree, listAll} from "@/api/system/user";

export default {
  name: "Team",
  dicts: [],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        deptName: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询部门列表 */
    getList() {
      this.loading = true;
      listAll().then(response => {
        this.deptList = this.handleTree(response.data, "userId", "fromUserId");
        this.loading = false;
      });
    }
  }
};
</script>
