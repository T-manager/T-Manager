import Vue from "vue";
import Router from "vue-router";
import todolist from "@/pages/todolist";
import test from "@/pages/test";


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
      path: "/test",
      name: "test",
      component: test
      
    }
  ]
});
