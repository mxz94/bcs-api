<template>
  <div style=" background-color: #f0f0f0;  min-height: 100vh; ">
    <van-image src="/images/add-banner.png"/>
    <van-form @submit="submitForm" class="m-[20px] py-[10px] bg-white rounded-[10px]">
      <van-cell-group inset>
        <van-field
            v-model="formData.name"
            placeholder="请填写姓名"
            clearable
            required
            label="姓名"
            maxlength="6"
            show-word-limit
        />
        <van-field
            v-model="formData.idCard"
            placeholder="请填写身份证号"
            clearable
            required
            label="身份证号"
            name = "pattern"
            :rules="[{ pattern, message: '身份证格式不正确' }]"
        />
        <van-field
            v-model="formData.phone"
            placeholder="请填写手机号"
            clearable
            required
            label="手机号"
            name = "validator"
            :rules="[{ validator, message: '手机号格式不正确' }]"
        />
        <van-field
            v-model="formData.taocanName"
            is-link
            required
            readonly
            name="picker"
            label="套餐选择"
            placeholder="请选择套餐"
            @click="showPicker = true"
            :rules="[{ required: true }]"
        />
        <van-popup v-model:show="showPicker" position="bottom">
          <van-picker
              show-toolbar="true"
              :columns="columns"
              @confirm="onConfirm"
              @cancel="showPicker = false"
          />
        </van-popup>
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block style="color: white; background: rgb(1, 71, 157); border-color: rgb(1, 71, 157);" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import {Toast} from "vant";
import {getToken, getUrlKey} from "@/libs/util";

export default {
  data() {
    return {
      showPicker: false,
      columns: [
      ],
      formData: {
        name: '',
        idCard: '',
        phone: '',
        taocanId: '',
        fromUserId: ''
      },
      pattern: /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i,
      patternPhone: /^1(3|4|5|6|7|8|9)\d{9}$/,
      errors: {},
      popupVisible: false
    };
  },
  mounted() {
    // this.$router.push({path : '/login', query: { redirect: location.hostname }});
    this.selectTaoCanList();
  },
  created() {
    let userId = getUrlKey("userId")
    this.formData.fromUserId = userId
  },
  methods: {
    validator(val) {
      return /^1(3|4|5|6|7|8|9)\d{9}$/.test(val)
    },
    selectTaoCanList() {
      let that =this
      this.$request(
          "/system/selectData/listAll",
          "get",
          null,
          {"type":"taocan"},
          function (res) {
            console.log(res)
            if (res.code == 200) {
              that.columns = res.data
            } else {
              Toast(res.msg)
            }
          }
      );
    },
    onConfirm(selectedOptions ) {
      this.showPicker = false
      this.formData.taocanName = selectedOptions.text
      this.formData.taocanId = selectedOptions.value
    },
    submitForm() {
      let that =this
      this.$request(
          "/apply/apply",
          "post",
          "json",
          that.formData,
          function (res) {
            console.log(res)
            if (res.code == 200) {
              that.columns = res.data
            } else {
              Toast(res.msg)
            }
          }
      );
    }
  }
};

</script>

<style lang="less">

.form-container {
  background-color: #f8f8f8;
  padding: 20px;
  box-sizing: border-box;
}

.form-title {
  color: #00a5e0;
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 10px;
}

.form-description {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
}

.form-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.form-item input {
  width: 100%;
  height: 40px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  padding-left: 10px;
  font-size: 14px;
}

.submit-btn {
  width: 100%;
  height: 40px;
  background-color: #00a5e0;
  color: #fff;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
}

.popup {
  background-color: rgba(0, 0, 0, 0.6);
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
}

.popup-content {
  background-color: #fff;
  width: 100%;
  max-width: 300px;
  padding: 20px;
  text-align: center;
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
}

</style>