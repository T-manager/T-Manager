<template>
  <div style="display:flex; justify-content:center; margin-bottom:30px">
    <!-- 顶栏 -->
    <v-card
      style="width:1392px; height:51px; box-shadow: 0px 1px 6px 0px rgba(0, 0, 0, 0.26); border-radius:20px; display:flex; align-items:center; margin-top:16px"
    >
      <!-- <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon> -->
      <div
        style="margin-left:29px; cursor:pointer; font-size:20px; color:#535353"
        @click="gotoProjectPage"
      >
        T-Manager
      </div>
      <div
        style="display:flex; margin-left:37px; align-items:center"
        v-if="$route.path.split('/')[1] == 'projectdetail'"
      >
        <div
          style="margin-right:17px;"
          :class="
            $route.path == '/home/teaforence' ? 'choose_home' : 'unchoose_home'
          "
          @click="gotoTodoList()"
        >
          Dashboard
        </div>

        <div style="height:23px;">
          <v-divider vertical></v-divider>
        </div>
        <div
          style="margin-left:17px;"
          :class="
            $route.path == '/home/community' ? 'choose_home' : 'unchoose_home'
          "
          @click="gotoGantt()"
        >
          Gantt
        </div>
      </div>
      <v-spacer></v-spacer>
      <!-- <SearchingBarthird></SearchingBarthird> -->
      <div style="margin-left:29px; margin-right:15px; cursor:pointer;">
        <!-- <v-avatar size="30" color="#aaa" @click="gotoLogin()">
          <div style="height:20px; width:20px; background-color:#aaaaaa"></div>
        </v-avatar> -->
        <UserCard size="37"></UserCard>
      </div>
    </v-card>
  </div>
</template>

<script>
// import SearchingBarthird from "@/components/SearchingBarthird.vue";
import UserCard from "@/components/UserCard.vue";
export default {
  components: {
    //   SearchingBarthird,
    UserCard
  },
  data() {
    return {
      currentPath: ""
    };
  },
  methods: {
    gotoProjectPage() {
      this.$router.replace({ path: "/project" });
    },
    showUser() {
      if (this.user.avatar == null) this.$store.state.show.showLogin = true;
    },
    gotoLogin() {
      this.$router.replace({ path: "/login" });
    }
  },
  watch: {
    $route(to, from) {
      // 从URL解析出当前是哪个页面
      var urls = this.$route.path.split("/");
      this.currentPath = urls[urls.length - 1];
    }
  }
};
</script>

<style scoped>
.profilePhoto {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 2rpx solid indigo;
}
.topbar {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.teaforence_text {
  width: 50px;
  height: 30px;
}
.choose_home {
  color: #101010;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  padding-top: 6px;
  border-bottom: #428675 6px solid;
  line-height: 43px;
}
.unchoose_home {
  color: #707070;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  line-height: 49px;
}
</style>
