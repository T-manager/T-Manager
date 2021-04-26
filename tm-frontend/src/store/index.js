import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
Vue.use(Vuex);
export default new Vuex.Store({
  state: {
    token: null,
    host: "http://1.15.171.21/api/",
    show: {
      showModifyTodo: false
    }
  },
  getters: {
    getToken() {
      console.log(localStorage.getItem("token"));
      return localStorage.getItem("token");
    },
    getUsername() {
      return localStorage.getItem("username");
    },
    getUserphoto: state => {
      return localStorage.getItem("userphoto");
    },
    getScroll: state => state.scrollHeight
  },
  mutations: {
    set_token(state, token) {
      localStorage.setItem("token", token);
    },
    del_token() {
      localStorage.removeItem("token");
    },
    set_username(state, token) {
      localStorage.setItem("username", token);
    },
    del_username() {
      localStorage.removeItem("username");
    },
    set_userphoto(state, token) {
      localStorage.setItem("userphoto", token);
    },
    del_userphoto() {
      localStorage.removeItem("userphoto");
    },
    response(state, token) {
      console.log("token");
      console.log(typeof token);
      if (token.response) {
        alert(token.response.data.message);
        // console.log(typeof token.response.data.status);
        if (token.response.data.status == 401) {
          var path = "/login";
          router.push({ path: path });
        }
      } else if (token.request) {
        alert(token.request.data.message);
        if (token.request.data.status == 401) {
          var path = "/login";
          router.push({ path: path });
        }
      } else {
        alert(token.message);
      }
    }
  },
  actions: {},
  modules: {}
});
