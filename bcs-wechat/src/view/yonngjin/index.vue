<template>
  <div>
    <van-nav-bar

        title="账单记录"
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
        <van-cell-group inset  v-for="item in list" :key="item.id" >
          <van-cell >
            <!-- 使用 right-icon 插槽来自定义右侧图标 -->
            <van-row class="record-row">
              <van-col span="12" class="record-item">
                <span class="label">办理ID:</span> {{ item.recordId }}
              </van-col>
              <van-col span="12" class="record-item">
                <span class="label">用户:</span> {{ item.recordNickName }}
              </van-col>
              <van-col span="12" class="record-item">
                <span class="label">推荐人:</span> {{ item.tuijianName }}
              </van-col>
              <van-col span="12" class="record-item">
                <span class="label">佣金:</span>
                <span :class="{'positive-amount': item.fee > 0, 'negative-amount': item.fee < 0}">
                  ¥{{ item.fee || 0 }}
                </span>
              </van-col>
              <van-col span="24" class="record-item">
                <span class="label">金额变动:</span> ¥{{ item.oldBalance }} -> ¥{{ item.newBalance }}
              </van-col>
              <van-col span="24" class="record-item">
                <span class="label">创建时间:</span> {{ item.createTime }}
              </van-col>
              <van-row v-if="item.remark" class="record-remark">
                <van-col span="24" class="record-item">
                  <span class="label">备注:</span> {{ item.remark }}
                </van-col>
              </van-row>
            </van-row>
          </van-cell>
        </van-cell-group>
      </van-list>
<!--    </van-pull-refresh>-->
  </div>
</template>

<script>
import {Toast } from 'vant';
import {red} from "tailwindcss/colors";

export default {
  data() {
    return {
      searchObj: {
        pageNum: 1,
        pageSize: 10,
      },
      value: '',
      total: 0,
      list: [],
      loading: true,
      finished: false,
    };
  },
created() {
  this.searchObj.pageNum = 1
  this.searchObj.pageSize = 10
  this.list = []
  this.onLoad()
},
  computed: {
    red() {
      return red
    }

  },
  onRefresh() {

  },
  methods: {
    onLoad() {
      // return () => {
      setTimeout(() => {
        let that =this
        this.$request(
            "/callFeeRecord/list",
            "get",
            null,
            {"type":1, "pageNum": that.searchObj.pageNum},
            function (res) {
              console.log(res)
              if (res.code == 200) {
                that.list = that.list.concat(res.rows)
                console.log(that.list)
                that.loading = false
                that.total = res.total
                that.searchObj.pageNum++
                if (that.list.length >= that.total) {
                  that.finished = true
                } else {
                  that.searchObj.pageNum++
                }
              } else {
                Toast({type: "fail ", message: res.msg})
              }
            }
        );
      })
      // }
    },
    onClickLeft() {
      history.back();
    }
  }
};
</script>

<style lang="less">
.record-row {
  padding: 12px;
  background-color: #f0f4f7; /* 轻微的背景颜色，提升整体层次 */
  border-radius: 10px; /* 圆角边框让样式更柔和 */
  margin-bottom: 12px; /* 每条记录之间增加间距 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); /* 阴影效果，提升立体感 */
}

.record-item {
  padding: 6px 0;
  font-size: 14px;
  color: #333; /* 主要字体颜色 */
}

.label {
  font-weight: bold;
  color: #555; /* 标签字体颜色更深，突出 */
  margin-right: 6px; /* 标签和内容之间的间距 */
}

.record-item span {
  display: inline-block;
  vertical-align: middle; /* 标签和文字垂直居中 */
}

.record-item {
  border-bottom: 1px solid #e0e0e0; /* 增加分隔线 */
  margin-bottom: 8px; /* 元素之间的间隔 */
}

.record-item:last-child {
  border-bottom: none; /* 最后一项不加分隔线 */
}
/* 动态颜色样式 */
.positive-amount {
  color: green; /* 正数显示绿色 */
}
.negative-amount {
  color: red; /* 负数显示红色 */
}



</style>
