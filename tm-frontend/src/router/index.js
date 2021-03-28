import Vue from "vue";
import Router from "vue-router";
import todolist from "@/pages/todolist";
import test from "@/pages/test";
import register from "@/pages/register";

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "todolist",
      component: todolist
    },
    {
      path: "/todolist",
      name: "todolist",
      component: todolist
    },
    {
      path: "/register",
      name: "register",
      component: register
    },
    {
      path: "/test",
      name: "test",
      component: test
      
    }
  ]
});
