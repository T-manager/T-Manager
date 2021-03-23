<template>
  <v-container class="d-flex" style="flex-direction:column">
    <v-btn @click="addTodolost">添加todolist</v-btn>
    <v-btn @click="addProject">添加project</v-btn>
    <v-btn @click="getTodo" class="success">获取todo</v-btn>
    <v-btn @click="addTodo" class="error">添加todo</v-btn>
    <v-btn @click="deleteTodo" class="error">删除todo</v-btn>
    <v-btn @click="getTodolist" class="success">获取todolist</v-btn>


  </v-container>
</template>

<script>
export default {
  data() {
    return {
      newTodolist: { todolistName: "这是名字" },
      newTodo: {
        todolistId: 1,
        todoName: "name name!!!!!",
        todoDetail: "detail detail!!!",
        todoMember: 1
      }
    };
  },
  methods: {
    addTodolost: function() {
      this.$axios({
        method: "post",
        url: "http://localhost:6767/api/todolist/1",
        data: {
          todolistName: "title title!!!!!"
        }
      })
        .then(res => {
          alert("成功提交，谢谢您的配合！");

          this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    },

    addProject: function() {
      this.$axios({
        method: "post",
        url: "http://localhost:6767/api/project/add",
        data: this.newTodo
      })
        .then(res => {
          alert("成功提交，谢谢您的配合！");

          this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    },

    addTodo: function() {
      this.$axios({
        method: "post",
        url: "http://localhost:6767/api/todo/add",
        data: this.newTodo
      })
        .then(res => {
          alert("成功提交，谢谢您的配合！");
          console.log(res);
          // this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    },
    getTodo: function() {
      this.$axios({
        method: "get",
        url: "http://localhost:6767/api/todo/get/1"
      })
        .then(res => {
          console.log(res);
          // this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    },

    getTodolist: function() {
      this.$axios({
        method: "get",
        url: "http://localhost:6767/api/todolist/get/1"
      })
        .then(res => {
          console.log(res);
          // this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    },
    deleteTodo: function() {
      this.$axios({
        method: "delete",
        url: "http://localhost:6767/api/todo/delete/1"
      })
        .then(res => {
          console.log(res);
          // this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    }
  }
};
</script>

<style scoped>
.success{
  background-color:green;
}
.error{
  background-color:red;
}
</style>
