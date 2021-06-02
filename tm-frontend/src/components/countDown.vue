<template>
  <div>
    <v-btn
      v-if="!showTime"
      @click="changeShowTime"
      text
      style="font-weight:600; font-size:24px;"
      :color="
        todo.todoDdlShort == 'Today'
          ? 'indigo'
          : todo.todoCheck
          ? 'green lighten-2'
          : 'red lighten-2'
      "
    >
      <div
        style="width:280px; margin-right:45px"
        v-if="todo.todoDdlShort == 'Today'"
      >
        <v-divider></v-divider>
      </div>
      {{ todo.todoDdlShort }}</v-btn
    >

    <v-btn
      v-if="showTime"
      @click="changeShowTime"
      text
      style="font-weight:600; font-size:40px;text-transform: none;"
      :color="
        todo.todoDdlShort == 'Today'
          ? 'indigo'
          : todo.todoCheck
          ? 'green lighten-2'
          : 'red lighten-2'
      "
    >
      <div v-if="leftTime > 0 && !todo.todoCheck">
        {{ day }}d {{ hour }}h {{ min }}min {{ second }}s
      </div>
      <div v-if="todo.todoCheck">
        Well done!
      </div>
      <div v-if="leftTime <= 0 && !todo.todoCheck">
        Overdue!
      </div>
    </v-btn>
  </div>
</template>
<script>
export default {
  data: function() {
    return {
      day: "0",
      hour: "00",
      min: "00",
      second: "00",
      showTime: false,
      leftTime: 0
    };
  },
  created() {
    this.countTime();
  },
  props: ["curStartTime", "todo"],
  methods: {
    changeShowTime() {
      if (this.todo.todoDdlShort != "Today") {
        this.showTime = !this.showTime;
      }
    },
    // 倒计时
    countTime() {
      // 获取当前时间
      let date = new Date();
      let now = date.getTime();
      // 设置截止时间
      let endDate = new Date(this.curStartTime); // this.curStartTime需要倒计时的日期
      let end = endDate.getTime();
      // 时间差
      this.leftTime = end - now;
      // 定义变量 d,h,m,s保存倒计时的时间
      if (this.leftTime >= 0) {
        // 天
        this.day = Math.floor(this.leftTime / 1000 / 60 / 60 / 24);
        // 时
        let h = Math.floor((this.leftTime / 1000 / 60 / 60) % 24);
        this.hour = h < 10 ? "0" + h : h;
        // 分
        let m = Math.floor((this.leftTime / 1000 / 60) % 60);
        this.min = m < 10 ? "0" + m : m;
        // 秒
        let s = Math.floor((this.leftTime / 1000) % 60);
        this.second = s < 10 ? "0" + s : s;
      } else {
        this.day = 0;
        this.hour = "00";
        this.min = "00";
        this.second = "00";
      }
      // 等于0的时候不调用
      if (
        Number(this.hour) === 0 &&
        Number(this.day) === 0 &&
        Number(this.min) === 0 &&
        Number(this.second) === 0
      ) {
        return;
      } else {
        // 递归每秒调用countTime方法，显示动态时间效果,
        setTimeout(this.countTime, 1000);
      }
    }
  }
};
</script>
<style></style>
