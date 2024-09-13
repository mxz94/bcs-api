<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
          </div>
        </el-card>
      </el-col> -->
      <el-col :span="18" :offset="2">
        <el-card style="padding:118px 0">
          <!-- <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div> -->
          <div class="text-center">
            <userAvatar :user="user" />
          </div>
          <userInfo :user="user" @loadData="loadData" />
          <!-- <resetPwd /> -->
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Profile",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      });
    },
    loadData() {
      this.getUser()
    }
  }
};
</script>
