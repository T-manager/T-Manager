<template>
  <div v-if="show" style="margin:15px">
    <v-card class="todolist">
      <div style="display:flex; padding: 0px 30px 0px 30px;">
        <div class="todolist_info">
          <div style="font-size:24px;">{{ todolist.todolistName }}</div>
          <div>
            Task Achievement: {{ todolist.todolistCompleteNum }}/{{
              todolist.todolistTotalNum
            }}
          </div>
        </div>
        <v-spacer></v-spacer>
        <div style="display:flex; align-items:flex-start">
          <modifyTodoDialog :todolist="todolist"></modifyTodoDialog>
          <v-btn
            icon
            @click="deleteTodolist"
            :loading="loading.delete"
            :disabled="loading.delete"
          >
            <v-icon>mdi-close</v-icon></v-btn
          >
        </div>
      </div>

      <v-list flat>
        <v-subheader>All</v-subheader>
        <v-list-item-group multiple>
          <v-list-item
            v-for="(todo, index) in todolist.todos"
            :key="index"
            @click=""
          >
            <template>
              <v-list-item-content
                style="display:flex; justify-content:flex-start; margin-left:10px;"
              >
                <div
                  class="todo_name"
                  :style="
                    todo.todoCheck
                      ? 'color:#a3a3a3; text-decoration:line-through'
                      : 'color:#434843'
                  "
                >
                  {{ todo.todoName }}
                </div>
                <div
                  style="display:flex; margin-top:5px"
                  :style="
                    todo.todoCheck
                      ? 'color:#c3c3c3; text-decoration:line-through'
                      : 'color:#838383'
                  "
                >
                  <div class="todo_detail">
                    {{ todo.todoDetail }}
                  </div>
                  <div class="todo_ddl">
                    {{ todo.todoDdl }}
                  </div>
                </div>
              </v-list-item-content>

              <v-list-item-action style="margin-right:8px">
                <v-checkbox
                  v-model="todo.todoCheck"
                  color="primary"
                ></v-checkbox>
              </v-list-item-action>
            </template>
          </v-list-item>
          <!-- add new todo -->
          <addTodoDialog :todolist="todolist"></addTodoDialog>
        </v-list-item-group>
      </v-list>
    </v-card>
  </div>
</template>

<script>
import addTodoDialog from "@/components/addTodoDialog";
import modifyTodoDialog from "@/components/modifyTodoDialog";
export default {
  data: function() {
    return {
      show: true,
      loading: {
        delete: false
      }
    };
  },
  methods: {
    deleteTodolist() {
      this.loadAddTodoList = true;
      this.$axios({
        method: "delete",
        url:
          this.$store.state.host + "todolist/delete/" + this.todolist.todolistId
      })
        .then(res => {
          this.show = false;
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    }
  },
  props: ["todolist"],
  components: {
    addTodoDialog,
    modifyTodoDialog
  }
};
</script>

<style>
.todolist {
  width: 400px;
  padding: 30px 0px 10px 0px;
}
.todolist_info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.todo_name {
  text-align: left;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo_ddl {
  margin-left: 5px;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo_detail {
  width: 200px;
  text-align: left;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.omit {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
