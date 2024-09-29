<template>
  <div>
    <van-cell-group>
      <van-cell>
        <div class="node" @click="toggle">
<!--          <span class="level">{{ node.level }}</span>-->
          <span class="name">{{ node.nickName }}</span>
          <div class="record-info">
            <van-col span="6" class="record-item">
              <span class="label">团队总和:</span> ¥{{ node.huafeiTeamTotal || 0 }}
            </van-col>
            <van-col span="6" class="record-item">
              <span class="label">分成比例:</span> {{ node.huafeiTeamTotalRate || 0 }}%
            </van-col>
            <van-col span="6" class="record-item">
              <span class="label">下级分成:</span> ¥{{ node.huafeiSubFenTotal || 0 }}
            </van-col>
            <van-col span="6" class="record-item">
              <span class="label">我的分成:</span> ¥{{ node.myFenTotal || 0 }}
            </van-col>
          </div>
          <span class="toggle" v-if="node.children && node.children.length">
            {{ isOpen ? '[-]' : '[+]' }}
          </span>
        </div>
      </van-cell>
    </van-cell-group>
    <div class="children" v-if="isOpen && node.children && node.children.length">
      <tree-node
          v-for="child in node.children"
          :key="child.id"
          :node="child"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: "TreeNode",
  props: {
    node: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isOpen: false, // 控制展开/折叠状态
    };
  },
  methods: {
    toggle() {
      this.isOpen = !this.isOpen; // 切换状态
    },
  },
};
</script>

<style scoped>
.node {
  padding: 8px;
  margin: 4px 0;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between; /* 使内容分散对齐 */
}

.record-info {
  display: flex;
  flex-wrap: wrap;
}

.record-item {
  flex: 1;
  min-width: 120px;
  padding: 2px 8px;
}

.label {
  font-weight: bold;
}

.name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-right: 8px; /* 为名字和等级之间增加一点间距 */
}

.level {
  font-size: 14px;
  color: #888;
  margin-right: 8px; /* 为等级和名字之间增加一点间距 */
}

.toggle {
  cursor: pointer;
  color: #007aff; /* 设置折叠标志的颜色 */
}

.children {
  margin-left: 20px;
}
</style>
