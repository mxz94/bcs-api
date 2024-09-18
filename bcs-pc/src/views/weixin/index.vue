<template>
  <div class="container">
    <!-- 用户信息区域 -->
    <div class="user-info">
      <div class="user-header">
        <img class="avatar" src="path-to-avatar.png" alt="avatar" />
        <div class="user-details">
          <p class="user-name">代理</p>
          <p class="user-id">微信用户(ID: 10040)</p>
          <p class="user-referrer">推荐人: 无</p>
        </div>
      </div>
      <div class="user-balance">
        <div class="balance-item">
          <p>0</p>
          <p>现金</p>
        </div>
        <div class="balance-item">
          <p>0</p>
          <p>话费分成(16%)</p>
        </div>
      </div>
    </div>

    <!-- 按钮区域 -->
    <div class="options">
      <div class="option-item" @click="handleClick('billRecords')">
        <span>账单记录</span>
      </div>
      <div class="option-item" @click="handleClick('cashWithdraw')">
        <span>现金提现</span>
      </div>
      <div class="option-item" @click="handleClick('cashWithdrawRecords')">
        <span>现金提现记录</span>
      </div>
      <div class="option-item" @click="handleClick('feeShareRecords')">
        <span>话费分成记录</span>
      </div>
      <div class="option-item" @click="handleClick('feeShareWithdraw')">
        <span>话费分成提现</span>
      </div>
      <div class="option-item" @click="handleClick('feeShareWithdrawRecords')">
        <span>话费分成提现记录</span>
      </div>
      <div class="option-item" @click="handleClick('faq')">
        <span>常见问题</span>
      </div>
      <div class="option-item" @click="handleClick('myTeam')">
        <span>我的团队</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    handleClick(option) {
      switch (option) {
        case 'billRecords':
          // 跳转到账单记录页面
          console.log("账单记录页面");
          break;
        case 'cashWithdraw':
          // 跳转到现金提现页面
          console.log("现金提现页面");
          break;
        case 'cashWithdrawRecords':
          // 跳转到现金提现记录页面
          console.log("现金提现记录页面");
          break;
        case 'feeShareRecords':
          // 跳转到话费分成记录页面
          console.log("话费分成记录页面");
          break;
        case 'feeShareWithdraw':
          // 跳转到话费分成提现页面
          console.log("话费分成提现页面");
          break;
        case 'feeShareWithdrawRecords':
          // 跳转到话费分成提现记录页面
          console.log("话费分成提现记录页面");
          break;
        case 'faq':
          // 跳转到常见问题页面
          console.log("常见问题页面");
          break;
        case 'myTeam':
          // 跳转到我的团队页面
          console.log("我的团队页面");
          break;
      }
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  background-color: #f5f5f5;
}

.user-info {
  background-color: #0a52b5;
  color: white;
  padding: 20px;
  border-radius: 10px;
}

.user-header {
  display: flex;
  align-items: center;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.user-details {
  margin-left: 10px;
}

.user-name {
  font-size: 1.2em;
  font-weight: bold;
}

.user-id, .user-referrer {
  font-size: 0.9em;
}

.user-balance {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.balance-item p {
  margin: 0;
}

.options {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  background-color: white;
  margin-top: 20px;
  padding: 10px;
  border-radius: 10px;
}

.option-item {
  width: 45%;
  margin: 10px 0;
  padding: 10px;
  background-color: #f0f0f0;
  text-align: center;
  border-radius: 8px;
  cursor: pointer;
}

.option-item span {
  font-size: 0.9em;
  color: #333;
}

.option-item:hover {
  background-color: #e0e0e0;
}
</style>
