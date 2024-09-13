<template>
  <el-dialog title="修改密码" :visible.sync="centerDialogVisible" width="30%" center>
    <el-form ref="form" :model="user" :rules="rules" label-width="80px">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="user.oldPassword" placeholder="请输入旧密码" type="password" show-password />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="user.newPassword" placeholder="请输入新密码" type="password" show-password />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="user.confirmPassword" placeholder="请确认新密码" type="password" show-password />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" @click="close">取 消</el-button>
      <el-button type="primary" :loading="btnLoading" @click="submit">确 认</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateUserPwd } from "@/api/system/user";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      centerDialogVisible: false,
      btnLoading: false,
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: "旧密码不能为空", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    show() {
      this.centerDialogVisible = true
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.btnLoading = true
          updateUserPwd(this.user.oldPassword, this.user.newPassword).then(response => {
            this.btnLoading = false
            this.centerDialogVisible = false
            this.$modal.msgSuccess("修改成功");
          });
        }
      });
    },
    close() {
      // this.$tab.closePage();
      this.centerDialogVisible = false
      this.$refs["form"].resetFields()
    }
  }
};
</script>
