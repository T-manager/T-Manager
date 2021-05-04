<template>
  <div>
    <!-- add todo button -->
    <v-list-item style="margin-top:10px" @click="showAddTodo = true">
      <v-list-item-content style="display:flex; justify-content:center;"
        ><v-icon>mdi-plus</v-icon>
      </v-list-item-content>
    </v-list-item>
    <!-- add todo popup -->
    <v-dialog v-model="showAddTodo" persistent max-width="600px">
      <v-card class="card-background" style="padding: 30px 35px 50px 35px;">
        <div
          style="font-size:30px; margin-left:10px; width:100%; text-align:left; display:flex; align-items:center"
        >
          Create New Todo
          <v-btn color="primary" style="margin-left:10px" icon>
            <v-icon size="28">mdi-help-circle-outline</v-icon>
          </v-btn>
        </div>
        <v-card-text style="margin-top:30px; padding:10px">
          <!-- choose todo name -->
          <div
            style="display:flex; flex-direction:row; font-size:16px; color:#101010; margin-bottom:20px"
          >
            <v-icon>mdi-subtitles-outline</v-icon>
            <div style="margin-right:10px; margin-left:10px">
              {{ todolist.projectName }}
            </div>
            <div style="color:#bdbdbd; font-size:22px">/</div>
            <div style="margin-left:10px">{{ todolist.todolistName }}</div>
          </div>
          <v-text-field
            label="todo name"
            color="primary"
            :rules="rules.nameRules"
            counter="20"
            prepend-icon="mdi-alphabetical"
            v-model="todoForm.todoName"
          ></v-text-field>
          <!-- choose todo detail -->
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
          <!-- choose todo deadline -->
          <div style="display:flex">
            <!-- deadline date -->
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
            <!-- deadline time -->
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
          <!-- choose todo executer -->
          <v-autocomplete
            v-if="projectType == 'team'"
            v-model="todoForm.todoMember"
            :items="projectMembers"
            :loading="loadingMember"
            :disabled="loadingMember"
            label="todo executer"
            color="primary"
            prepend-icon="mdi-badge-account-outline"
          >
          </v-autocomplete>
        </v-card-text>
        <div class="card_action">
          <v-btn
            depressed
            style="border:#cccccc solid 1px; color:#777777; width:120px"
            @click="showAddTodo = false"
          >
            <v-icon class="pr-2">mdi-cancel</v-icon>Cancel
          </v-btn>
          <v-btn
            depressed
            color="primary"
            style="color:#fff; width:120px; margin-left:50px; "
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
      loadingMember: true,
      showAddTodo: false,
      todoForm: {
        todoMember: this.$store.getters.getUsername // set the creater as the executer as default
      },
      todo: {},
      projectMembers: [],
      projectType: "",
      dateFormat: new Date().toISOString().substr(0, 10),
      datePicker: false,
      timePicker: false,
      // inline check rules
      rules: {
        nameRules: [
          v =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 1) ||
            "the length of name should be 1-20"
        ],
        detailRules: [
          v =>
            (typeof v != "undefined" && v.length <= 100) ||
            "the length of detail should be less than 100"
        ],
        notNull: [v => typeof v != "undefined" || "please enter"]
      }
    };
  },
  props: ["projectId", "todolist"],
  methods: {
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 1;
    },
    checkDetailRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 100;
    },
    checkDateTimeRules(v) {
      return typeof v != "undefined";
    },
    /** check all rules */
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
    /** create todo method */
    async createTodo() {
      this.loading = true;
      this.todoForm.todoDdl = this.todo.date + " " + this.todo.time + ":00";
      if (!this.checkRules()) {
        // have problem, reject submit
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
  },
  created() {
    // get projectType
    this.loadingMember = true;
    this.$axios({
      method: "get",
      url: this.$store.state.host + "project/get/" + this.projectId,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        this.projectType = res.data.data.projectType;
        if (this.projectType == "team") {
          // get project member if is team project
          this.$axios({
            method: "get",
            url: this.$store.state.host + "relation/getuser/" + this.projectId,
            headers: {
              Authorization: "Bearer " + this.$store.getters.getToken
            }
          })
            .then(res => {
              for (var i in res.data.data)
                this.projectMembers.push(res.data.data[i].memberName);
              this.loadingMember = false;
            })
            .catch(error => {
              this.$store.commit("response", error);
            });
        } else {
          this.loadingMember = false;
        }
      })
      .catch(error => {
        this.$store.commit("response", error);
      });
  }
};
</script>

<style scoped>
.card-background {
  background-image: url("../assets/TmanagerLogo_l5.svg");
  background-size: 520px;
  background-repeat: no-repeat;
  background-position: 140px -65px;
}
.card_action {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}
</style>
