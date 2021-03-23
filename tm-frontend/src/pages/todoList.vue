<template>
  <div style="display:flex; padding:50px">
    <todolistCard :todolist="todolist"></todolistCard>
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
        @click="replyDis"
        :loading="loadReply"
        :disabled="loadReply"
        >New</v-btn
      >
      <v-btn
        v-if="showAddTodolist == true"
        color="primary"
        icon
        @click="(showAddTodolist = false), (newTodolistName = '')"
        style="margin-left:8px"
        ><v-icon>mdi-close</v-icon></v-btn
      >
    </v-card>
    <addTodoListDialog></addTodoListDialog>
  </div>
</template>

<script>
import todolistCard from "@/components/todolistCard";
export default {
  data: function() {
    return {
      showAddTodolist: true,
      newTodolistName: "",
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
    }
  },

  components: {
    todolistCard
  }
};
</script>

<style scoped>
.plusTodoList {
  margin-left: 30px;
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
