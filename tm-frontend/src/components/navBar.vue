<template>
  <div
    style="display:flex; justify-content:center; margin-bottom:30px; width:100%; padding: 0px 70px 0px 70px"
  >
    <v-card
      style="width:100%; height:51px; box-shadow: 0px 1px 6px 0px rgba(0, 0, 0, 0.26); border-radius:20px; display:flex; align-items:center; margin-top:16px"
    >
      <div
        style="margin-left:18px; cursor:pointer; font-size:20px; color:#535353; display:flex; align-items:center"
        @click="gotoProjectPage"
      >
        <v-img
          :src="require('../assets/TmanagerLogo.svg')"
          height="35"
          width="35"
          contain
          style="margin-right:10px;"
        ></v-img>
        T-Manager
      </div>
      <div
        style="display:flex; margin-left:37px; align-items:center"
        v-if="$route.path.split('/')[1] == 'projectdetail'"
      >
        <div
          style="margin-right:17px;"
          :class="
            $route.path.split('/')[2] == 'todolist'
              ? 'choose_home'
              : 'unchoose_home'
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
            $route.path.split('/')[2] == 'gantt'
              ? 'choose_home'
              : 'unchoose_home'
          "
          @click="gotoGantt()"
        >
          Gantt
        </div>
      </div>
      <v-spacer></v-spacer>
      <div style="font-size:20px">{{ $store.getters.getUsername }}</div>
      <!-- user avatar and a list of router -->
      <div
        style="margin-left:25px; margin-right:15px; cursor:pointer;"
        v-if="$store.getters.getUsername != null"
      >
        <userCard size="37"></userCard>
      </div>
      <router-link
        to="/login"
        v-else
        style="text-decoration:underline; margin-right:20px; color:#6271c2; cursor:pointer"
      >
        Please Sign In First
      </router-link>
    </v-card>
  </div>
</template>

<script>
import userCard from "@/components/userCard.vue";
export default {
  components: {
    userCard
  },
  data() {
    return {
      currentPath: ""
    };
  },
  methods: {
    // navigate methods
    gotoProjectPage() {
      if (this.$store.getters.getUsername != null)
        this.$router.replace({ path: "/project" });
    },
    gotoTodoList() {
      this.$router.replace({
        path:
          "/projectdetail/todolist/projectid=" +
          this.$route.path.split("projectid=")[1]
      });
      this.$router.go(0);
    },
    gotoGantt() {
      this.$router.replace({
        path:
          "/projectdetail/gantt/projectid=" +
          this.$route.path.split("projectid=")[1]
      });
      this.$router.go(0);
    },
    gotoLogin() {
      this.$router.replace({ path: "/login" });
    },

    /** show/hide user card method */
    showUser() {
      if (this.user.avatar == null) this.$store.state.show.showLogin = true;
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
  border-bottom: #7f8ccd 6px solid;
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
