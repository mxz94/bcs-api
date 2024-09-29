<template>
  <div class="login-page">
    <van-form @submit="onSubmit" class="login-container">
      <van-cell-group inset>
        <van-field
            v-model="username"
            name="用户名"
            label="用户名"
            placeholder="用户名"
            :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
            v-model="password"
            type="password"
            name="密码"
            label="密码"
            placeholder="密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
        <van-field name="switch" label="注册并登录">
          <template #input>
            <van-switch v-model="checked" />
          </template>
        </van-field>

      </van-cell-group>

      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import { Toast } from 'vant';
import {setToken} from "@/libs/util";
export default {
  name: "Login",
  data() {
    return {
      username: '',
      password: '',
      checked: false,
      tenantId: 3,
      url: {
        login: "/login",
        logRegister: "/logRegister",
      },
    };
  },
  methods: {
    onSubmit() {
      /*console.log('submit', values);*/
      this.loginByCode(this.username, this.password);
    },
    loginByCode: function (userName, password) {
      let that =this
      this.$request(
          this.checked ? this.url.logRegister : this.url.login,
          "post",
          "json",
          { username: userName, password:password, tenantId: this.tenantId },
          function (res) {
            if (res.code == 200) {
              let token = res.data.token;
              setToken(token);
              if (that.$route.query.redirect )
              {
                that.$router.push(that.$route.query.redirect);
              } else {
                that.$router.push({ path: "/user" });
              }
              Toast("提交成功")
            } else {
              Toast(res.msg)
              if(1001!==res.code) {
                // getWeiXinCode("scantips");
              }
            }
            // that.$storage.delData("wxcode");
          }
      );
    },
    toRegister(){
      this.$router.push('/Register')
    }
  }
}

</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: url('/images/qr_bg.jpeg') no-repeat center center fixed; /* 背景图片 */
  background-size: cover; /* 背景覆盖整个容器 */
}

.login-container {
  width: 90%;
  max-width: 400px; /* 最大宽度 */
  padding: 20px;
  border-radius: 10px; /* 圆角边框 */
  background-color: rgba(255, 255, 255); /* 半透明白色背景 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.logo {
  text-align: center;
  margin-bottom: 20px;
}

.footer {
  margin-top: 20px;
  display: flex;
  justify-content: space-between; /* 按钮左右分布 */
  font-size: 14px; /* 字体大小 */
}

</style>

