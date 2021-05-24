<template>
  <v-menu
    open-on-hover
    rounded="lg"
    nudge-bottom="57"
    nudge-right="-85"
    close-delay="800"
  >
    <!-- The user avatar on the Navbar -->
    <template v-slot:activator="{ on }">
      <v-avatar
        :size="size"
        v-on="on"
        @mouseover="showCard = true"
        @mouseleave="showCard = false"
        @click="gotoCalender"
        style="background-color:#aaaaaa"
      >
        <v-img
          :src="
            $store.state.host + 'auth/images/' + $store.getters.getUserphoto
          "
          height="35"
          width="35"
        ></v-img>
      </v-avatar>
    </template>

    <!-- Menu that appears when you hover over the Avatar -->
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
        <v-list-item link to="/calender" class="list_item_center">
          Calender
        </v-list-item>
        <v-list-item @click="openHelp" class="list_item_center">
          Help
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
    openHelp() {
      window.open("https://ecb29d.baklib-free.com", "_blank");
    },
    /**get personal information for a specific user*/
    getDetail() {
      this.user.username = this.$store.getters.getUsername;
      if (this.user.username == null) return;
      this.showContent = false;
      this.$axios({
        method: "get",
        url: this.$store.state.host + "user/" + this.user.username,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.user = res.data.data;
          // console.log("Userinfo", this.user);
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
    /** go to user profile page*/
    toUserPage() {
      if (this.user.username != null) {
        var path = "/profile";
        this.$router.push({ path: path });
      } else {
        this.$store.state.show.showLogin = true;
      }
    },
    gotoCalender() {
      this.$router.replace({
        path: "/calender"
      });
    },
    /**user logout*/
    logout() {
      this.$store.commit("del_token");
      this.$store.commit("del_username");
      this.$store.commit("del_userphoto");
      alert("Log out successfully！");
      var path = "/login";
      this.$router.push({ path: path });
      this.$router.go(0);
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
