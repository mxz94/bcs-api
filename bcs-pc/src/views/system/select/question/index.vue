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
          v-hasPermi="['system:selectData:list']"
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
      <el-table-column label="问题名称" align="center" prop="name" />
      <el-table-column label="排序" align="center" prop="sortNum" />
<!--      <el-table-column label="问题大拿" align="center" prop="remark" />-->
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
            v-hasPermi="['system:selectData:list']"
          >编辑</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            type="text"
            @click="setStatus(scope.row, 1)"
            v-hasPermi="['system:selectData:list']"
          >禁用</el-button>
          <el-button
            v-else
            type="text"
            @click="setStatus(scope.row, 0)"
            v-hasPermi="['system:selectData:list']"
          >启用</el-button>
          <el-button
            type="text"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:selectData:list']"
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
    <el-dialog :title="title" :visible.sync="open" width="800px" :destroy-on-close="true" >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="问题名称:" prop="name">
          <el-input v-model="form.name" placeholder="请输入问题名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sortNum">
          <el-input   type="number" :rows="2" v-model="form.sortNum" placeholder="请输入值数字"  value="0"/>
        </el-form-item>
        <el-form-item label="问题答案:" prop="remark">
<!--          <el-input   type="textarea" :rows="2" v-model="form.remark" placeholder="问题答案" />-->
          <editor id='tinymce' v-model='form.remark' :init='init'></editor>
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
import tinymce from 'tinymce/tinymce'
import 'tinymce/themes/silver/theme'
import Editor from '@tinymce/tinymce-vue'
import "tinymce/plugins/lists"; //超链接
import "tinymce/plugins/link"; //超链接
import "tinymce/plugins/table"; //超链接
import 'tinymce/plugins/paste'  //粘贴过滤
import 'tinymce/plugins/preview'  //预览
import 'tinymce/plugins/media'
import 'tinymce/plugins/image'
import 'tinymce/plugins/code'
import 'tinymce/plugins/textcolor'
import 'tinymce/plugins/wordcount'
import 'tinymce/plugins/contextmenu'
import "tinymce/plugins/advlist"; //高级列表
import "tinymce/plugins/anchor"; //锚点
import "tinymce/plugins/autolink"; //自动链接
import "tinymce/plugins/autoresize"; //编辑器高度自适应,注：plugins里引入此插件时，Init里设置的height将失效
// import 'tinymce/plugins/autosave'  //自动存稿
import "tinymce/plugins/charmap"; //特殊字符
import "tinymce/plugins/code"; //编辑源码
import "tinymce/plugins/codesample"; //代码示例
import "tinymce/plugins/directionality"; //文字方向
import "tinymce/plugins/fullpage"; //文档属性
import "tinymce/plugins/fullscreen"; //全屏
import "tinymce/plugins/help"; //帮助
import "tinymce/plugins/hr"; //水平分割线
import "tinymce/plugins/image"; //插入编辑图片
import "tinymce/plugins/importcss"; //引入css
import "tinymce/plugins/insertdatetime"; //插入日期时间
import "tinymce/plugins/link"; //超链接
import "tinymce/plugins/lists"; //列表插件
import "tinymce/plugins/media"; //插入编辑媒体
import "tinymce/plugins/nonbreaking"; //插入不间断空格
import "tinymce/plugins/pagebreak"; //插入分页符
import "tinymce/plugins/contextmenu";
import "tinymce/plugins/paste"; //粘贴插件
import "tinymce/plugins/preview"; //预览
import "tinymce/plugins/print"; //打印
import "tinymce/plugins/quickbars"; //快速工具栏
import "tinymce/plugins/save"; //保存
import "tinymce/plugins/searchreplace"; //查找替换
// import 'tinymce/plugins/spellchecker'  //拼写检查，暂未加入汉化，不建议使用
import "tinymce/plugins/tabfocus"; //切入切出，按tab键切出编辑器，切入页面其他输入框中
import "tinymce/plugins/table"; //表格
import "tinymce/plugins/template"; //内容模板
import "tinymce/plugins/textcolor"; //文字颜色
import "tinymce/plugins/textpattern"; //快速排版
import "tinymce/plugins/toc"; //目录生成器
import "tinymce/plugins/visualblocks"; //显示元素范围
import "tinymce/plugins/visualchars"; //显示不可见字符
import "tinymce/plugins/wordcount";
import {uploadAvatar, uploadFile} from "@/api/system/user";
import store from "@/store"; //字数统计
export default {
  name: "SelectQuestion",
  dicts: ['sys_common_status'],
  data() {
    return {
      init: {
        selector: `#tinymce`,
        automatic_uploads: false,
        language_url: '/tinymce/zh_CN.js',
        language: 'zh_CN',
        skin_url: '/tinymce/skins/ui/oxide',
        height: 300,
        plugins: "print preview contextmenu searchreplace autolink directionality  formulas indent2em visualblocks visualchars fullscreen image axupimgs link media template code codesample table charmap hr pagebreak nonbreaking anchor insertdatetime advlist lists wordcount textpattern formatpainter",
        toolbar:
          'fullscreen undo redo restoredraft | forecolor backcolor bold italic underline strikethrough link anchor formatpainter | alignleft aligncenter alignright alignjustify | outdent indent indent2em | \\\n' +
          '                styleselect formatselect fontselect fontsizeselect lineheight | table image axupimgs media | bullist numlist | blockquote subscript superscript removeformat charmap  | \\\n' +
          '                 hr pagebreak insertdatetime | selectall cut copy paste pastetext | print preview searchreplace visualblocks code formulas',
        branding: false,
        zindex: 10000,
        fontsize_formats: "12px 14px 16px 18px 20px 22px 24px 26px 28px 30px 32px 36px 40px 44px 48px 56px 64px 72px", //字体大小
        font_formats: "宋体=SimSun;黑体=SimHei;楷体=KaiTi;仿宋=FangSong;Times New Roman=Times New Roman;微软雅黑=Microsoft Yahei;苹方=PingFang SC;隶书=LiSu;",
        lineheight_formats: "0.5 0.8 1 1.2 1.5 1.75 2 2.5 3 4 5", //行高配置，也可配置成"12px 14px 16px 18px"这种形式
        paste_data_images: true, //图片是否可粘贴
        images_upload_handler: (blobInfo, success, failure) => {
          if (blobInfo.blob().size / 1024 / 1024 > 2) {
            failure("上传失败，图片大小请控制在 2M 以内");
          } else {
            let formData = new FormData();
            formData.append("file", blobInfo.blob());
            uploadFile(formData).then(res => {
                if (res.code == 200) {
                  success(res.data.url); //上传成功，在成功函数里填入图片路径
                } else {
                  if (res.msg) {
                    failure(res.msg);
                  } else {
                    failure("上传失败");
                  }
                }
            });

            // axios.post(`${uploadApi}/uploadimg`, params, config).then((res) => {
            //   if (res.data.code == 200) {
            //     success(res.data.msg); //上传成功，在成功函数里填入图片路径
            //   } else {
            //     if (res.data.msg) {
            //       failure(res.data.msg);
            //     } else {
            //       failure("上传失败");
            //     }
            //   }
            // }).catch(() => {
            //   failure("上传出错，服务器开小差了呢");
            // });
          }
        },
      },
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
  mounted() {
    tinymce.init({})
  },
  components: {Editor},
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
          console.log(this.form)
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
<style>
/* 在el-dialog中tinymce z-index 被太小而被遮挡时要加这两句 */
.tox-tinymce-aux{z-index:99999 !important;}
.tinymce.ui.FloatPanel{z-Index: 99;}
</style>
