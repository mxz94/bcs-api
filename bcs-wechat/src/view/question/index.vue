<template>
  <div>
  <van-nav-bar
      title="常见问题"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
  />
  <div class="faq-container"  >
    <div v-for="(faq, index) in faqList" :key="index" class="faq-item">
      <!-- 问题部分 -->
      <div class="faq-question" @click="toggleAnswer(index)">
        <span>{{ faq.name }}</span>
        <span class="arrow" :class="{ open: faq.name }">
          {{ faq.name ? '▲' : '▼' }}
        </span>
      </div>
      <!-- 回答部分 -->
      <div v-if="faq.value ? true : false" class="faq-answer" v-html="faq.remark">
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import {Toast} from "vant";
import {getWeiXinCode, isWeiXinBrowser} from "@/libs/wx-util";
import {getToken, getUrlKey} from "@/libs/util";

export default {
  data() {
    return {
      tenantId: 3,
      faqList: [
        // 更多问题...
      ]
    };
  },
  created() {
    // 获取当前url
    let url = window.location.href;
    let prefix = url.match(/https:\/\/([^.]+)\./)[1];
    if (prefix == "wx") {
      this.tenantId = 3
    } else if (prefix == "wx1") {
      this.tenantId = 4
    } else if (prefix == "wx2") {
      this.tenantId = 5
    } else if (prefix == "wx3") {
      this.tenantId = 6
    }
    if (isWeiXinBrowser()) {
      this.selectTaoCanList()
    } else {
      window.location.href =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=888";
    }
  },
  methods: {
    selectTaoCanList() {
      let that =this
      this.$request(
          "/system/selectData/listAll2",
          "get",
          null,
          {"type":"question", "tenantId": this.tenantId},
          function (res) {
            console.log(res)
            if (res.code == 200) {
              that.faqList = res.data
            } else {
              Toast(res.msg)
            }
          }
      );
    },
    toggleAnswer(index) {
      console.log(!this.faqList[index].value)
      this.faqList[index].value = !this.faqList[index].value;
    },
    onClickLeft() {
      history.back();
    }
  }
};
</script>

<style scoped>
.faq-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.faq-title {
  text-align: center;
  margin-bottom: 20px;
}

.faq-item {
  margin-bottom: 10px;
  border-bottom: 1px solid #ddd;
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  font-weight: bold;
  background-color: #f7f7f7;
}

.faq-answer {
  padding: 10px;
  background-color: #fff;
}

.arrow {
  transition: transform 0.2s ease;
}

.arrow.open {
  transform: rotate(180deg);
}
</style>
