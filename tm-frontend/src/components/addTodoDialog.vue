<template>
  <div>
    <v-list-item style="margin-top:10px" @click="showAddTodo = true">
      <v-list-item-content style="display:flex; justify-content:center;"
        ><v-icon>mdi-plus</v-icon>
      </v-list-item-content>
    </v-list-item>
    <v-dialog v-model="showAddTodo" persistent max-width="600px">
      <v-card class="card">
        <v-card-title>
          <span class="headline">Create a new todo</span>
        </v-card-title>
        <v-card-text>
          <v-text-field
            label="todo list name"
            color="primary"
            prepend-icon="mdi-leaf"
            v-model="todolist.todolistName"
            readonly
          ></v-text-field>
          <v-text-field
            label="todo name"
            color="primary"
            :rules="rules.nameRules"
            counter="20"
            prepend-icon="mdi-alphabetical"
            v-model="todoForm.todoName"
          ></v-text-field>
          <v-textarea
            label="todo detail"
            required
            color="primary"
            :rules="rules.detailRules"
            counter="100"
            auto-grow
            rows="1"
            prepend-icon="mdi-menu"
            v-model="todoForm.todoDetail"
          ></v-textarea>
          <!-- 选择时间日期 -->
          <div style="display:flex">
            <!-- 选择ddl日期 -->
            <v-menu
              ref="datePicker"
              v-model="datePicker"
              :close-on-content-click="false"
              :return-value.sync="todo.date"
              transition="scale-transition"
              offset-y
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="todo.date"
                  label="todo ddl date"
                  color="primary"
                  :rules="rules.notNull"
                  prepend-icon="mdi-calendar-outline"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker
                v-model="todo.date"
                no-title
                scrollable
                locale="zh-cn"
                :first-day-of-week="1"
                color="primary"
              >
                <v-spacer></v-spacer>
                <v-btn
                  color="indigo lighten-4"
                  @click="datePicker = false"
                  class="indigo--text"
                  >Cancel</v-btn
                >
                <v-btn
                  color="primary"
                  class="white--text"
                  @click="$refs.datePicker.save(todo.date)"
                  >Submit</v-btn
                >
              </v-date-picker>
            </v-menu>
            <!-- 选择ddl时间 -->
            <v-menu
              ref="timePicker"
              v-model="timePicker"
              :close-on-content-click="false"
              :nudge-right="40"
              :return-value.sync="todo.time"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="todo.time"
                  label="todo ddl time"
                  color="primary"
                  :rules="rules.notNull"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-time-picker
                v-if="timePicker"
                v-model="todo.time"
                full-width
                format="24hr"
                color="primary"
                @click:minute="$refs.timePicker.save(todo.time)"
              ></v-time-picker>
            </v-menu>
          </div>

          <!-- 选择负责人 -->
        </v-card-text>
        <div class="card_action">
          <v-btn
            color="indigo lighten-5"
            class="indigo--text"
            @click="showAddTodo = false"
          >
            <v-icon class="pr-2">mdi-cancel</v-icon>Cancel
          </v-btn>
          <v-btn
            color="primary"
            style="color:#fff; margin-left:20px"
            @click="createTodo()"
            :loading="loading"
            :disabled="loading"
          >
            <v-icon class="pr-2">mdi-upload-outline</v-icon>Submit
          </v-btn>
        </div>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      showAddTodo: false,
      todoForm: {
        todoCheck: true
      },
      todo: {},

      dateFormat: new Date().toISOString().substr(0, 10),
      datePicker: false,
      timePicker: false,
      rules: {
        nameRules: [
          v =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 3) ||
            "the length of name should be 3-20"
        ],
        detailRules: [
          v =>
            (typeof v != "undefined" && v.length >= 5 && v.length <= 100) ||
            "the length of detail should be 5-100"
        ],
        notNull: [v => typeof v != "undefined" || "please enter"]
      }
    };
  },
  props: ["todolist"],
  methods: {
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 3;
    },
    checkDetailRules(v) {
      if (typeof v == "undefined") return false;
      return v.length >= 5 && v.length <= 100;
    },
    checkDateTimeRules(v) {
      return typeof v != "undefined";
    },
    checkRules() {
      if (!this.checkNameRules(this.todoForm.todoName)) {
        alert("check the name");
        return false;
      }
      if (!this.checkDetailRules(this.todoForm.todoDetail)) {
        alert("check the detail");
        return false;
      }
      if (!this.checkDateTimeRules(this.todo.date)) {
        alert("check the date");
        return false;
      }
      if (!this.checkDateTimeRules(this.todo.time)) {
        alert("check the time");
        return false;
      }
      return true;
    },
    async createTodo() {
      this.loading = true;
      this.todoForm.todoDdl = this.todo.date + " " + this.todo.time + ":00";
      if (!this.checkRules()) {
        this.loading = false;
        return;
      }
      this.todoForm.todolistId = this.todolist.todolistId;
      await this.$axios({
        method: "post",
        url: this.$store.state.host + "todo/add",
        data: this.todoForm,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.loading = false;
          this.showAddTodo = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loading = false;
        });
    }
  }
};
</script>

<style scoped>
.card {
  padding: 20px;
}
.card_action {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
</style>
