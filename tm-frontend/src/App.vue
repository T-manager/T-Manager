<template>
  <v-app style="background-color:#f1f2f8;">
    <!-- 导航栏 -->
    <div
      style="position:fixed; width:100%; display:flex; justify-content:center; z-index:2"
    >
      <NavBar></NavBar>
    </div>
    <v-main style="padding-top:100px; padding-bottom:55px">
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import Vue from "vue";
import NavBar from "@/components/NavBar";
export default Vue.extend({
  name: "App",
  provide() {
    return {};
  },
  components: {
    NavBar
  }
});
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
