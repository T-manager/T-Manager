<template>
  <div style="display:flex; padding:35px">
    <div v-for="(todolist, index) in todolists" :key="index">
      <todolistCard :todolist="todolist"></todolistCard>
    </div>
    <!-- <todolistCard :todolist="todolist"></todolistCard> -->
    <v-card class="plusTodoList">
      <v-icon
        @click="showAddTodolist = true"
        v-if="showAddTodolist == false"
        large
        >mdi-plus</v-icon
      >
      <v-text-field
        v-model="newTodolistName"
        v-if="showAddTodolist == true"
        label="Enter a name"
        color="primary"
        style="margin-top:10px; margin-left:13px"
      ></v-text-field>
      <v-btn
        v-if="showAddTodolist == true"
        style="width:75px; height:35px; border-radius:15px; color:#fff; margin-left:10px"
        depressed
        color="primary"
        @click="addTodolist"
        :loading="loadAddTodoList"
        :disabled="loadAddTodoList"
        >New</v-btn
      >
      <v-btn
        v-if="showAddTodolist == true"
        color="primary"
        icon
        @click="(showAddTodolist = false), (newTodolistName = '')"
        style="margin-left:8px"
        :loading="loadAddTodoList"
        :disabled="loadAddTodoList"
        ><v-icon>mdi-close</v-icon></v-btn
      >
    </v-card>
  </div>
</template>

<script>
import todolistCard from "@/components/todolistCard";
export default {
  data: function() {
    return {
      showAddTodolist: false,
      loadAddTodoList: false,
      newTodolistName: "",
      todolists: "",
      todolist: {
        todolistId: 0,
        projectId: 0,
        todolistName: "这学期的ddl",
        todolistTotalNum: 3,
        todolistCompleteNum: 1,
        todos: [
          {
            todoId: 0,
            todolistId: 0,
            todoName: "CPT202中期",
            todoDetail: "CPT202项目的中期汇报",
            todoDdl: "2021-5-21 12:00:00",
            todoMember: 0,
            todoCheck: false
          },
          {
            todoId: 1,
            todolistId: 0,
            todoName: "CPT208第一次pre",
            todoDetail:
              "CPT208的第一次presentation，CPT208的第一次presentation",
            todoDdl: "2021-3-17 12:00:00",
            todoMember: 1,
            todoCheck: true
          },
          {
            todoId: 2,
            todolistId: 0,
            todoName: "CPT208第二次pre",
            todoDetail: "CPT208第二次presentation，讲prototyping",
            todoDdl: "2021-4-14 12:00:00",
            todoMember: 0,
            todoCheck: false
          }
        ]
      }
    };
  },
  methods: {
    showAddNewTodolist() {
      if (this.showAddTodolist == false) this.showAddTodolist = true;
    },
    hideAddNewTodolist() {
      this.showAddTodolist = false;
    },
    addTodolist() {
      this.loadAddTodoList = true;
      this.$axios({
        method: "post",
        url: this.$store.state.host + "todolist/add",
        data: {
          projectId: 0,
          todolistName: this.newTodolistName
        }
      })
        .then(res => {
          this.loadAddTodoList = false;
          this.showAddTodo = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loadAddTodoList = false;
        });
    }
  },
  created() {
    this.$axios({
      method: "get",
      url: this.$store.state.host + "todolist/get/0"
    })
      .then(res => {
        console.log(res);
        this.todolists = res.data.data;
      })
      .catch(error => {
        this.$store.commit("response", error);
      });
  },
  components: {
    todolistCard
  }
};
</script>

<style scoped>
.plusTodoList {
  margin: 15px;
  width: 400px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0px 10px 0px 10px;
}
.add_new {
  width: 600px;
  height: 800px;
}
</style>
