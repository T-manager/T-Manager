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
        <v-img src="https://picsum.photos/200"></v-img>
      </v-avatar>
    </template>

    <v-card
      style="height:207px; width:160px; padding-top:16px; background-color:#eeeeee; display:flex; flex-direction:column"
    >
      <router-link to="/register">注册</router-link>
      <router-link to="/login">登录</router-link>
      <router-link to="/my">个人主页</router-link>
      <router-link @click="logout">退出登录</router-link>
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
      user: {}
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
          this.showContent = true;
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    },
    gotoUserInfo() {
      this.$router.replace({
        path: "/user"
      });
    },
    toUserPage() {
      console.log("user");
      console.log(this.user);
      if (this.user.username != null) {
        var path = "/profile";
        this.$router.push({ path: path });
      } else {
        this.$store.state.show.showLogin = true;
      }
    },
    logout() {
      this.$store.commit("del_token");
      this.$store.commit("del_username");
      this.$store.commit("del_userphoto");
      alert("Log out successfully！")
      this.$router.go(0);
      return;
    }
  }
};
</script>

<style scoped></style>
