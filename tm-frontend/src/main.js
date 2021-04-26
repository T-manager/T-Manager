// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";
import Vuetify from "vuetify";
import vuetify from "@/plugins/vuetify";
import axios from "axios";
import store from "./store";

Vue.config.productionTip = false;
Vue.prototype.$axios = axios;

/* eslint-disable no-new */
Vue.use(Vuetify);

new Vue({
  vuetify,
  store,
  el: "#app",
  router,
  components: { App },
  template: "<App/>"
});

// axios.defaults.baseURL = "http://localhost:6767/";
// axios.defaults.headers.common["Authorization"] =
//   "Bearer " + localStorage.getItem("token");
// axios.defaults.headers.post["Content-Type"] =
//   "application/x-www=form-urlencoded";
