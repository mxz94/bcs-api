<template>
  <div class="team-tree">
  <van-nav-bar
      title="我的团队"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
  />
    <tree-node
        v-for="node in treeData"
        :key="node.id"
        :node="node"
    />
  </div>
</template>

<script>
import TreeNode from "@/components/TreeNode/index.vue";

export default {
  components: {
    TreeNode,
  },
  data() {
    return {
      treeData: [],
    };
  },
  created() {
    let that =this
    this.$request(
        "/system/user/teamTree",
        "get",
        null,
        null,
        function (res) {
          console.log(res)
          if (res.code == 200) {
            that.treeData = res.data
          } else {
            Toast(res.msg)
            if(1001!==res.data.code) {
              // getWeiXinCode("scantips");
            }
          }
          // that.$storage.delData("wxcode");
        }
    );
  },
  methods: {
    onClickLeft() {
      history.back();
    },
  },
};
</script>

<style>
.team-tree {
  font-family: Arial, sans-serif;
  padding: 16px;
}
</style>
