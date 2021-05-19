<template>
  <div>
    <!-- todo information (each line in todo list) -->
    <v-list-item
      @click="showTodoDetail = true"
      style="border-bottom:1px solid #dfdfdf;"
    >
      <template>
        <v-list-item-content
          style="display:flex; justify-content:flex-start; margin-left:10px; padding:7px 0px 7px 0px"
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
            <!-- show if overdue -->
            <v-chip
              v-if="
                !todo.todoCheck &&
                  todo.todoDdl.split(' ')[0] <
                    new Date().getFullYear() +
                      '-' +
                      (new Date().getMonth() > 8
                        ? new Date().getMonth()
                        : '0' + (new Date().getMonth() + 1)) +
                      '-' +
                      (new Date().getDate() > 9
                        ? new Date().getDate()
                        : '0' + new Date().getDate())
              "
              color="red"
              outlined
              small
              style="margin-left:10px"
              >Overdue</v-chip
            >
          </div>
          <div
            style="display:flex; margin-top:5px; color:#838383; align-items:center"
          >
            <v-avatar
              v-if="projectType == 'team'"
              size="25"
              :style="
                todo.todoCheck
                  ? 'margin-right:10px; border:solid 2px #333333'
                  : 'margin-right:10px; border:solid 2px #d0021b'
              "
            >
              <v-img
                :src="
                  $store.state.host + 'auth/images/' + todo.todoMemberAvatar
                "
                height="35"
                width="35"
                :gradient="
                  todo.todoCheck
                    ? 'to top right, rgba(150,150,150,.33), rgba(25,25,25,.7)'
                    : ''
                "
              ></v-img>
            </v-avatar>
            <div class="todo_detail" style="display:flex; align-items:center">
              <div style="margin-right:10px; " v-if="projectType == 'team'">
                {{ todo.todoMember }}
              </div>
              <div style="font-size:12px; margin-top:3px">
                {{ todo.todoDdl.split(" ")[0] }}
              </div>
            </div>
          </div>
        </v-list-item-content>
        <!-- todo checkbox -->
        <v-list-item-action
          style="margin-right:8px"
          v-if="
            $store.getters.getUsername == todo.todoMember ||
              $store.getters.getUsername == projectOwner
          "
        >
          <v-checkbox
            v-model="todo.todoCheck"
            color="primary"
            @click.stop="checkTodo()"
          ></v-checkbox>
        </v-list-item-action>
      </template>
    </v-list-item>

    <!-- todo detail popup -->
    <v-dialog v-model="showTodoDetail" max-width="550px">
      <v-card
        v-if="showModifyTodo == false"
        style="padding: 30px 35px 50px 35px;"
        class="card-background"
      >
        <div
          style="width:100%; display:flex; justify-content:flex-end; margin-top:-15px; margin-left:20px"
        >
          <v-icon @click="showTodoDetail = false">mdi-close</v-icon>
        </div>
        <div
          style="font-size:30px; margin-left:10px; width:100%; text-align:left; display:flex; align-items:center"
        >
          Todo Detail
          <!-- modify todo popup -->
          <v-btn
            v-if="
              $store.getters.getUsername == todo.todoMember ||
                $store.getters.getUsername == projectOwner
            "
            color="primary"
            @click="showModifyTodo = true"
            style="margin-left:10px"
            icon
          >
            <v-icon>mdi-pencil-outline</v-icon>
          </v-btn>
          <!-- delete todo -->
          <v-btn
            v-if="
              $store.getters.getUsername == todo.todoMember ||
                $store.getters.getUsername == projectOwner
            "
            @click="showPopupMethod"
            icon
            ><v-icon>mdi-delete-outline</v-icon></v-btn
          >
        </div>
        <!-- todo detail -->
        <v-card-text
          style="display:flex; flex-direction:column; margin-top:15px; padding:10px; color:#434343"
        >
          <div class="todo_detail_info">
            <div style="width:150px">Todolist Name:</div>
            <div style="width:350px; color:#838383">{{ todolistName }}</div>
          </div>
          <div class="todo_detail_info">
            <div style="width:150px">Todo Name:</div>
            <div style="width:350px; color:#838383">{{ todo.todoName }}</div>
          </div>
          <div class="todo_detail_info">
            <div style="width:150px">Detail:</div>
            <div style="width:350px; color:#838383">{{ todo.todoDetail }}</div>
          </div>
          <div class="todo_detail_info">
            <div style="width:150px">Todo Deadline:</div>
            <div style="width:350px; color:#838383">{{ todo.todoDdl }}</div>
          </div>
          <div class="todo_detail_info" v-if="projectType == 'team'">
            <div style="width:150px">Executer:</div>
            <div style="width:350px; color:#838383">{{ todo.todoMember }}</div>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- modify todo popup -->
    <v-dialog v-model="showModifyTodo" persistent max-width="600px">
      <v-card style="padding: 30px 35px 50px 35px;" class="card-background">
        <div
          style="font-size:30px; margin-left:10px; width:100%; text-align:left"
        >
          Modify Todo
        </div>
        <v-card-text style="margin-top:30px; padding:10px">
          <div
            style="display:flex;flex-direction:row;font-size:16px;color:#101010;margin-top:20px;margin-bottom:20px"
          >
            <v-icon>mdi-subtitles-outline</v-icon>
            <div style="margin-right:10px;margin-left:10px">
              {{ projectName }}
            </div>
            <div style="color:#bdbdbd;font-size:22px">/</div>
            <div style="margin-left:10px">{{ todolistName }}</div>
          </div>

          <v-text-field
            label="todo name"
            color="primary"
            :rules="rules.nameRules"
            counter="20"
            prepend-icon="mdi-alphabetical"
            v-model="todo.todoName"
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
            v-model="todo.todoDetail"
          ></v-textarea>
          <!-- change todo deadline -->
          <div style="display:flex">
            <!-- change deadline date -->
            <v-menu
              ref="datePicker"
              v-model="datePicker"
              :close-on-content-click="false"
              :return-value.sync="todoChange.date"
              transition="scale-transition"
              offset-y
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="todoChange.date"
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
                v-model="todoChange.date"
                no-title
                scrollable
                locale="zh-cn"
                :first-day-of-week="1"
                color="primary"
              >
                <v-spacer></v-spacer>
                <v-btn
                  color="indigo lighten-5"
                  @click="datePicker = false"
                  class="indigo--text"
                  >Cancel</v-btn
                >
                <v-btn
                  color="primary"
                  class="white--text"
                  @click="$refs.datePicker.save(todoChange.date)"
                  >Confirm</v-btn
                >
              </v-date-picker>
            </v-menu>
            <!-- change deadline time -->
            <v-menu
              ref="timePicker"
              v-model="timePicker"
              :close-on-content-click="false"
              :nudge-right="40"
              :return-value.sync="todoChange.time"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="todoChange.time"
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
                v-model="todoChange.time"
                full-width
                format="24hr"
                color="primary"
                @click:minute="$refs.timePicker.save(todoChange.time)"
              ></v-time-picker>
            </v-menu>
          </div>
          <!-- choose todo executer -->
          <v-autocomplete
            v-if="projectType == 'team'"
            v-model="todo.todoMember"
            :items="projectMembers"
            :loading="loadingMember"
            :disabled="loadingMember"
            label="todo executer"
            color="primary"
            prepend-icon="mdi-badge-account-outline"
          >
          </v-autocomplete>
        </v-card-text>
        <div style="display:flex; justify-content:center; margin-top:10px">
          <!-- close modify popup -->
          <v-btn
            depressed
            style="border:#cccccc solid 1px; color:#777777; width:120px; margin-right:50px"
            @click="showModifyTodo = false"
          >
            <v-icon class="pr-2">mdi-cancel</v-icon>Cancel
          </v-btn>
          <!-- submit modification -->
          <v-btn
            depressed
            color="primary"
            style="color:#fff; width:120px"
            @click="modifyTodo()"
            :loading="loading"
            :disabled="loading"
          >
            <v-icon class="pr-2">mdi-upload-outline</v-icon>Confirm
          </v-btn>
        </div>
      </v-card>
    </v-dialog>
    <!-- delete confirmation popup -->
    <popup
      message="Are you sure you want to delete this todo list?"
      :showPopup="showPopup"
      @showPopupMethod="showPopupMethod"
      @confirmOperation="deleteTodo"
    ></popup>
  </div>
</template>

<script>
import popup from "@/components/popup";
export default {
  data() {
    return {
      loading: false,
      loadingMember: true,
      showTodoDetail: false,
      showModifyTodo: false,
      showPopup: false,
      todoChange: {},
      projectMembers: [],
      projectOwner: "",
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
  components: { popup },
  props: ["todolistName", "todo", "projectName", "projectId"],
  methods: {
    /** show popup method */
    showPopupMethod() {
      this.showPopup = !this.showPopup;
    },
    /** check/uncheck todo method */
    checkTodo() {
      var todoId = this.todo.todoId;
      this.todo.loading = true;
      this.$axios({
        method: "put",
        url: this.$store.state.host + "todo/check/" + todoId,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.todo.loading = false;
          this.$emit("changeCompleteNum"); // call the parent component method to change the number of tasks completed
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    },
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
    /** check all rules again beform submit */
    checkRules() {
      if (!this.checkNameRules(this.todo.todoName)) {
        alert("check the name");
        return false;
      }
      if (!this.checkDetailRules(this.todo.todoDetail)) {
        alert("check the detail");
        return false;
      }
      if (!this.checkDateTimeRules(this.todoChange.date)) {
        alert("check the date");
        return false;
      }
      if (!this.checkDateTimeRules(this.todoChange.time)) {
        alert("check the time");
        return false;
      }
      return true;
    },
    /** modifuy todo method */
    async modifyTodo() {
      this.loading = true;
      this.todo.todoDdl =
        this.todoChange.date + " " + this.todoChange.time + ":00 ";
      if (!this.checkRules()) {
        this.loading = false;
        return;
      }
      this.$axios({
        method: "put",
        url: this.$store.state.host + "todo/edit",
        data: this.todo,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.loading = false;
          this.showTodoDetail = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loading = false;
        });
    },
    /** delete todo method */
    deleteTodo() {
      this.$axios({
        method: "delete",
        url: this.$store.state.host + "todo/delete/" + this.todo.todoId,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    }
  },
  created() {
    // split datetime into date and time before show page
    var dateTime = this.todo.todoDdl;
    this.todoChange.date = dateTime.split(" ")[0];
    this.todoChange.time = dateTime.split(" ")[1].split(":00 ")[0];
    // get projectType
    this.loadingMember = true;
    this.$axios({
      method: "get",
      url: this.$store.state.host + "project/getV2/" + this.projectId,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        this.projectOwner = res.data.data.projectOwner;
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
.todo_name {
  text-align: left;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo_ddl {
  margin-left: 3px;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.todo_detail {
  width: 195px;
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
.card-background {
  background-image: url("../assets/TmanagerLogo_l5.svg");
  /* background-image: url("../assets/TManagerLogo.png"); */
  background-size: 520px;
  background-repeat: no-repeat;
  background-position: 140px -65px;
}
.todo_detail_info {
  text-align: left;
  margin-top: 15px;
  font-size: 18px;
  display: flex;
  justify-content: flex-start;
}
</style>
