<template>
  <div style=" background-color: #f0f0f0;  min-height: 100vh; ">
    <van-nav-bar

        title="话费分成提现"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
    />
    <div class="bg-white mt-[10px] mx-[10px] px-[10px] py-[20px] text-center">
      <div class="text-[#424242] text-[12px]">可提现话费分成</div>
      <div  class="text-[#000000] text-[18px] font-bold py-[10px]">¥{{ user.callBalance }}</div>
      <van-divider></van-divider>
      <div  class="text-[#000000] text-[12px]">注：最低提现60元, 提现个人所得税8.6%</div>
      <div  class="text-[#000000] text-[12px]">每月28日才可提现话费分成</div>
      <div class="mt-[10px]">
        <van-button @click="tixian" type="primary" size="large"  round style="color: white; background: rgb(1, 71, 157); border-color: rgb(1, 71, 157);">
          提现
        </van-button>
      </div>
      <div class="flex mt-[10px]">
        <van-uploader accept="image/*"  class="flex-1 py-[5px] mr-[5px] custom-uploader"  :before-read="beforeRead" :after-read="afterRead" >
          <van-button plain  size="normal" round block style="color: rgb(1, 71, 157); border-color: rgb(1, 71, 157);">上传收款码</van-button>
        </van-uploader>
        <div class="flex-1 py-[5px] ml-[5px]">
          <van-button @click="goPage" plain  size="normal" round block style="color: rgb(1, 71, 157); border-color: rgb(1, 71, 157); ">提现记录</van-button>
        </div>
      </div>
      <div class="mt-[20px]">
        <van-image :src="user.shoukuanUrl"></van-image>
      </div>
      <van-dialog v-model="showDialog" show-cancel-button title="" @confirm="onConfirm" @cancel="onCancel" class="pt-[30px]">
        <van-cell-group inset>
          <van-field
              v-model="tx.amount"
              type="digit"
              name="金额"
              label="提现金额"
              placeholder="金额"
          />
        </van-cell-group>
      </van-dialog>
    </div>
  </div>
</template>

<script>
import { Toast, showFailToast } from 'vant';
import {getToken} from "@/libs/util";

export default {
  data() {
    return {
      showDialog: false,
      user: {
      },
      tx: {
        amount: 0,
        type: 2
      }
    };
  },
  created() {
    var token = getToken();
    if (!token) {
      this.$router.push('/login');
    }
    this.getUser();
  },
  methods: {
    getUser() {
      let that =this
      this.$request(
          "/wechat/userInfo",
          "get",
          null,
          null,
          function (res) {
            console.log(res)
            if (res.code == 200) {
              that.user = res.data
            } else {
              Toast(res.msg)
            }
          }
      );
    },
    tixian() {
      this.tx.amount = null;
      this.showDialog = true;
    },
    onCancel() {
      this.$toast('取消');
      this.showDialog = false;
    },
    onConfirm() {
      if (this.tx.amount <= 0 || !this.tx.amount) {
        this.$toast('请输入金额');
        return
      }
      if (this.tx.amount > this.user.callBalance) {
        this.$toast('超过可提现金额');
        return
      }
      let that =this
      this.$request(
          "/withdrawRecord/tixian",
          "post",
          "json",
          that.tx,
          function (res) {
            console.log(res)
            if (res.code == 200) {
              that.$toast.success('提交成功');
              that.showDialog = false
              that.getUser();
            } else {
              Toast( res.msg)
            }
          }
      )
    },
    goPage() {
      this.$router.push('/hftixian');
    },
    beforeRead(file) {
      // 限制文件大小为 3MB
      const MAX_SIZE = 3 * 1024 * 1024; // 3MB
      if (file.size > MAX_SIZE) {
        this.$toast.fail('图片大小不能超过3MB');
        return false;  // 阻止文件上传
      }
      return true;  // 允许文件上传
    },
    async afterRead(file) {
      try {
        // 创建 FormData 用于上传文件
        const formData = new FormData();
        formData.append('file', file.file);  // `file.file` 是实际的文件对象
        let that = this;
        this.$request(
            "/common/uploadshoukuan",
            "post",
            "file",
            formData,
            (res) => {
              if (res.code == 200) {
                that.$toast.success('上传成功');
                that.user.shoukuanUrl = res.data.url;
              } else {
                Toast(res.msg);
              }
            }
        );
      } catch (error) {
        // 上传失败的处理
        this.$toast.fail('上传失败');
        console.error('上传失败:', error);
      }
    },
    onClickLeft() {
      history.back();
    },
  },
};
</script>

<style lang="less">
.custom-uploader .van-uploader__input-wrapper {
  width: 100%;
}
</style>
