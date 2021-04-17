<template>
  <v-menu
    open-on-hover
    rounded="lg"
    nudge-bottom="57"
    nudge-right="-85"
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

    <v-list style="padding:0px; width:120px" flat>
      <v-list-item-group style="padding:0px">
        <v-list-item
          link
          to="/login"
          class="list_item_center"
          v-if="$store.getters.getUsername == null"
        >
          Sign In
        </v-list-item>
        <v-list-item link to="/profile" class="list_item_center">
          Profile
        </v-list-item>
        <v-list-item class="list_item_center">
          个人日历
        </v-list-item>
        <v-list-item
          @click="logout"
          class="list_item_center"
          v-if="$store.getters.getUsername != null"
        >
          Sign Out
        </v-list-item>
      </v-list-item-group>
    </v-list>
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
      this.$axios({
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
      alert("Log out successfully！");
      var path = "/login";
      this.$router.push({ path: path });
      return;
    }
  }
};
</script>

<style scoped>
.list_item_center {
  margin: 0px 6px 0px 6px;
  display: flex;
  justify-content: center;
  border-bottom: 1px solid #cccccc;
}
</style>
