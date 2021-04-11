<template>
  <v-menu
    open-on-hover
    rounded="lg"
    nudge-bottom="57"
    nudge-right="-130"
    close-delay="800"
  >
    <template v-slot:activator="{ on }">
      <v-avatar
        :size="size"
        v-on="on"
        @mouseover="(showCard = true), getDetail()"
        @mouseleave="showCard = false"
        @click="toUserPage"
        style="background-color:#aaaaaa"
      >
      </v-avatar>
    </template>

    <v-card
      style="height:207px; width:160px; padding-top:16px; background-color:#eeeeee; display:flex; flex-direction:column"
    >
      <router-link to="/register">注册</router-link>
      <router-link to="/login">登录</router-link>
      <router-link to="/my">个人主页</router-link>
    </v-card>
  </v-menu>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      loading: false,
      showCard: false,
      showContent: false,
      showFollow: true,
      position: true,
      showPost: false,
      user: {},
      tea_module: [
        { module_name: "我的茶迹", module_router: 0 },
        { module_name: "我的茶馆", module_router: 1 },
        { module_name: "我的茶会", module_router: 2 }
      ],
      tea_new: [
        { new_name: "新建采茶", new_router: "" },
        { new_name: "新建茶帖", new_router: "" }
      ],
      str: {
        str_follow: ["取消关注", "关注"]
      }
    };
  },
  props: ["size"],
  methods: {
    getDetail() {
      this.user.username = this.$store.getters.getUsername;
      if (this.user.username == null) return;
      this.showContent = false;
      this.axios({
        // 获取发起人信息
        method: "get",
        url: this.$store.state.host + "user/" + this.user.username
      })
        .then(res => {
          this.user = res.data.data;
          //console.log("用户信息");
          //console.log(this.user);
          this.showContent = true;
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    },
    gotoMyTea(mod) {
      this.$router.replace({
        path:
          "/userpage/username=" + this.user.username + "&&" + mod.module_router
      });
    },
    gotoFollow(type) {
      this.$router.replace({
        path:
          "/follow/username=" +
          this.$route.path.split("username=")[1] +
          "&&" +
          type
      });
    },
    gotoUserInfo() {
      this.$router.replace({
        path: "/user"
      });
    },
    followUser() {
      this.loading = true;
      if (this.user.isFollowed) {
        // 取消关注
        this.axios({
          method: "delete",
          url: this.$store.state.host + "follows/" + this.user.username
        })
          .then(res => {
            //console.log("取消关注" + this.user.username);
            //console.log(res);
            this.user.isFollowed = false;
            this.loading = false;
          })
          .catch(error => {
            this.$store.commit("response", error);
            this.loading = false;
          });
      } else {
        this.axios({
          method: "post",
          url: this.$store.state.host + "follows/" + this.user.username
        })
          .then(res => {
            //console.log("关注" + this.user.username);
            //console.log(res);
            this.user.isFollowed = true;
            this.loading = false;
          })
          .catch(error => {
            this.$store.commit("response", error);
            this.loading = false;
          });
      }
    },
    toUserPage() {
      console.log("user");
      console.log(this.user);
      if (this.user.username != null) {
        var path = "/userpage/username=" + this.user.username;
        this.$router.push({ path: path });
      } else {
        this.$store.state.show.showLogin = true;
      }
    },
    logout() {
      this.$store.commit("del_token");
      this.$store.commit("del_username");
      this.$store.commit("del_userphoto");
      this.$router.go(0);
      return;
    },
    showTeaPost() {
      this.showPost = true;
    }
  }
};
</script>

<style scoped>
.rowC {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.tea_module {
  height: 95px;
  width: 95px;
  border-radius: 100px;
  border: 1px solid #96efd9;
  color: #428675;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  margin-left: 33px;
  cursor: pointer;
}
</style>
