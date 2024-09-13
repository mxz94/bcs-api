<template>
  <div>
    <el-form ref="form" :model="user" :rules="rules" label-width="80px" label-position="left"
      style="width:50%;margin:42px auto 0">
      <el-form-item label="昵称：" style="position:relative;">
        <el-input v-model="user.nickName" maxlength="120" :readonly="isReadonly" />
        <div v-if="!isReadonly" style="position:absolute;right:-25px;bottom: 0;">
          <el-tooltip class="item" effect="dark" content="若未填写昵称，则会和用户名保持一致" placement="top">
            <i class="el-icon-question" style="font-size:20px;color:#7F7F7F;margin-left: 10px;"></i>
          </el-tooltip>
        </div>
      </el-form-item>
      <el-form-item label="用户名：" :prop="!isReadonly ? 'userName' : ''">
        <el-input v-model="user.userName" maxlength="120" :readonly="isReadonly" />
      </el-form-item>
      <el-form-item label="角色：">
        <el-input v-model="user.roleName" :readonly="true" />
      </el-form-item>
      <el-form-item label="手机号：" :prop="!isReadonly ? 'phonenumber' : ''">
        <el-input v-model="user.phonenumber" maxlength="11" :readonly="isReadonly" />
      </el-form-item>
      <!-- <el-form-item label="性别">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item> -->
      <el-form-item v-if="!isReadonly" style="text-align:center;">
        <el-button type="info" style="width: 112px;margin-left: -65px;" @click="close">取消</el-button>
        <el-button type="primary" style="width: 112px;margin-left: 80px;" :loading="btnLoading"
          @click="submit">保存</el-button>
      </el-form-item>
      <el-form-item v-else style="text-align:center;">
        <el-button type="primary" style="width: 112px;margin-left: -65px;" @click="editInfo">编辑</el-button>
        <el-button type="primary" style="width: 112px;margin-left: 80px;" @click="updatePassword">修改密码</el-button>
      </el-form-item>
    </el-form>
    <resetPwd ref="resetPwd" />
  </div>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";
import resetPwd from "./resetPwd";

export default {
  props: {
    user: {
      type: Object
    }
  },
  components: { resetPwd },
  data() {
    return {
      isReadonly: true,
      btnLoading: false,
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        // email: [
        //   { required: true, message: "邮箱地址不能为空", trigger: "blur" },
        //   {
        //     type: "email",
        //     message: "请输入正确的邮箱地址",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        phonenumber: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.btnLoading = true
          updateUserProfile(this.user).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.btnLoading = false
            this.isReadonly = true
            this.$emit('loadData', true)
          });
        }
      });
    },
    close() {
      // this.$tab.closePage();
      this.$refs["form"].resetFields()
      this.isReadonly = true
      this.$emit('loadData', true)
    },
    editInfo() {
      this.isReadonly = false
      this.$refs["form"].resetFields()
    },
    updatePassword() {
      this.$refs.resetPwd.show()
    }
  }
};
</script>
