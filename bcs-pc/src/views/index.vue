<template>
  <div class="app-container home">
    <el-card :bordered="false" style="width: 100%" :bodyStyle="{ padding: '0px' }">
      <div slot="header"
        style="background: #1890ff;color: #fff;padding: 28px 0 27px 62px; font-size: 28px; font-weight:300;">
        <span>{{ title }}</span>
      </div>
      <div class="index-box" :style="{ backgroundImage: `url(${indexBG})` }">
        <div class="info-cont">
          <!-- <div style="color:#333333;font-size:28px;font-weight: bold;">欢迎登录</div>
                    <div style="margin:49px 0 48px;text-indent: 2em;">
                        {{ indexTip }}
                    </div>
                    <div>
                        企业官网：{{ platformUrl }}
                    </div> -->
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.8.5",
      indexBG: require("@/assets/images/index-bgi.png"),
      title: '欢迎登陆业务佣金系统'
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  .el-card {
    border-radius: 8px;
  }

  ::v-deep .el-card__header {
    padding: 0;
  }

  .index-box {
    height: 75vh;
    background-size: contain;
    background-repeat: no-repeat;
    background-position-x: center;
    position: relative;
  }

  .info-cont {
    width: 536px;
    font-size: 16px;
    font-weight: 400;
    color: #666666;
    position: absolute;
    top: 234px;
    left: 62px;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

