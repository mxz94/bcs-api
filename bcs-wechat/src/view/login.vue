<template>
  <div class="login-page">
    <van-loading size="24px">加载中...</van-loading>
  </div>
</template>

<script>
import { Toast } from 'vant';
import {getToken, getUrlKey, getUrlParam, setToken} from "@/libs/util";
import {getWeiXinCode, isWeiXinBrowser} from "@/libs/wx-util";
export default {
  name: "Login",
  data() {
    return {
      username: '',
      password: '',
      checked: false,
      tenantId: 3,
      url: {
        h5Login:"/wechat/h5/wechatLogin"
      },
    };
  },
  created() {
    // 获取当前url
    let url = window.location.href;
    let prefix = url.match(/https:\/\/([^.]+)\./)[1];
    if (prefix = "wx") {
      this.tenantId = 3
    } else if (prefix = "wx2") {
      this.tenantId = 4
    } else if (prefix = "wx3") {
      this.tenantId = 5
    } else if (prefix = "wx4") {
      this.tenantId = 6
    }
    if (isWeiXinBrowser()) {
      let wxcode = getUrlKey("code");
      let userId = getUrlKey("state");
      if (userId == "null" || userId == "undefined" ) userId = null
      if (wxcode && wxcode !== "") {
        this.loginByCode(wxcode, userId);
      } else {
          if (!getToken()) {
            // 获取微信code
            getWeiXinCode( userId);
          } else {
            if (userId) {
              this.$router.push({ path: "/apply?userId="+userId });
            } else {
              this.$router.push({ path: "/user" });
            }
          }
      }
    } else {
      window.location.href =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=888";
    }
  },
  methods: {
    onSubmit() {
      /*console.log('submit', values);*/
      // this.loginByCode(this.username, this.password);
    },
    loginByCode: function (wxCode, userId) {
      let that =this
      this.$request(
          // this.checked ? this.url.logRegister : this.url.login,
          this.url.h5Login,
          "post",
          null,
          // { username: userName, password:password, tenantId: this.tenantId },
          { code : wxCode, tenantId: this.tenantId },
          function (res) {
            if (res.code == 200) {
              let token = res.data.token;
              setToken(token);
              if (userId) {
                that.$router.push({ path: "/apply?userId="+userId });
              } else {
                that.$router.push({ path: "/user" });
              }
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
  //background: url('/images/qr_bg.jpeg') no-repeat center center fixed; /* 背景图片 */
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

