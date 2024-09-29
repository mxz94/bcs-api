<template>
  <div>
    <van-nav-bar

        title="提现记录"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
    />
    <van-empty v-if="list.length == 0 && finished" description="" />
    <van-list
        v-model:loading="loading"
        :immediate-check="false"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
    >
      <van-cell-group inset v-for="item in list" :key="item.id">
        <van-cell>
          <van-row class="record-row">
            <van-col span="12" class="record-item">
              <span class="label">提现类型:</span> {{ item.type_dictText }}
            </van-col>
            <van-col span="12" class="record-item">
              <span class="label" >状态:</span>
              <span  :class="{'positive-amount': item.status == 1, 'negative-amount': item.fee == 2}">
                  {{ item.status_dictText}}
                </span>
            </van-col>
            <van-col span="12" class="record-item">
              <span class="label">提现金额:</span> ¥{{ item.amount || 0 }}
            </van-col>
            <van-col span="12" class="record-item">
              <span class="label">税率:</span> {{ item.rate || 0 }}%
            </van-col>
            <van-col span="24" class="record-item">
              <span class="label">转账金额:</span> ¥{{ item.realAmount || 0 }}
            </van-col>
            <van-col span="24" class="record-item">
              <span class="label">金额变动:</span>
              <span class="balance-change">¥{{ item.oldBalance }} -> ¥{{ item.newBalance }}</span>
            </van-col>
            <van-col span="24" class="record-item" v-if="item.remark">
              <span class="label">备注:</span> {{ item.remark }}
            </van-col>
            <van-col span="24" class="record-item">
              <span class="label">创建时间:</span> {{ item.createTime }}
            </van-col>
            <van-col span="24" class="record-item">
              <span class="label">更新时间:</span> {{ item.updateTime }}
            </van-col>
          </van-row>
        </van-cell>
      </van-cell-group>
    </van-list>
  </div>
</template>

<script>
import { Toast } from 'vant';

export default {
  data() {
    return {
      searchObj: {
        pageNum: 1,
        pageSize: 10,
      },
      list: [],
      loading: true,
      finished: false,
    };
  },
  created() {
    this.onLoad();
  },
  methods: {
    onLoad() {
      setTimeout(() => {
        this.$request(
            "/withdrawRecord/list",
            "get",
            null,
            { type: 2, pageNum: this.searchObj.pageNum },
            (res) => {
              if (res.code == 200) {
                this.list = this.list.concat(res.rows);
                this.loading = false;
                if (this.list.length >= res.total) {
                  this.finished = true;
                } else {
                  this.searchObj.pageNum++;
                }
              } else {
                Toast(res.msg);
              }
            }
        );
      });
    },
    onClickLeft() {
      history.back();
    },
  },
};
</script>

<style lang="less">
.record-row {
  padding: 12px;
  background-color: #f0f4f7;
  border-radius: 10px;
  margin-bottom: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.record-item {
  padding: 6px 0;
  font-size: 14px;
  color: #333;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 8px;
}

.label {
  font-weight: bold;
  color: #555;
  margin-right: 6px;
}

.record-item:last-child {
  border-bottom: none;
}
.positive-amount {
  color: green; /* 正数显示绿色 */
}
.negative-amount {
  color: red; /* 负数显示红色 */
}
</style>
