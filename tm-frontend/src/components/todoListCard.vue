<template>
  <v-card class="todolist">
    <div class="todolist_info">
      <div style="font-size:24px;">{{ todolist.todolistName }}</div>
      <div>
        Task Achievement: {{ todolist.todolistCompleteNum }}/{{
          todolist.todolistTotalNum
        }}
      </div>
    </div>

    <v-list flat>
      <v-subheader>All</v-subheader>
      <v-list-item-group multiple>
        <v-list-item v-for="(todo, index) in todolist.todos" :key="index">
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
              <v-checkbox v-model="todo.todoCheck" color="primary"></v-checkbox>
            </v-list-item-action>
          </template>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-card>
</template>

<script>
export default {
  data: function() {
    return {
      settings: []
    };
  },
  props: ["todolist"]
};
</script>

<style>
.todolist {
  width: 400px;
  padding: 30px 0px 30px 0px;
}
.todolist_info {
  padding: 0px 30px 0px 30px;
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
