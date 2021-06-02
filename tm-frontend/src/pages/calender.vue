<template>
  <div style="display:flex; padding:35px; justify-content:center;">
    <v-timeline :dense="$vuetify.breakpoint.smAndDown">
      <v-timeline-item
        :color="
          todo.todoDdlShort == 'Today'
            ? 'indigo'
            : todo.todoCheck
            ? 'green lighten-3'
            : 'red lighten-2'
        "
        v-for="(todo, index) in todos"
        :key="index"
        :left="todo.todoCheck"
        :right="!todo.todoCheck"
        :small="todo.todoCheck"
        :large="todo.todoDdlShort == 'Today'"
        :icon="todo.todoDdlShort == 'Today' ? 'mdi-star' : ''"
      >
        <template v-slot:opposite>
          <countDown :curStartTime="todo.todoDdl" :todo="todo"> </countDown>
        </template>
        <v-btn
          text
          color="indigo"
          style="width:400px; margin-top:10px"
          v-if="todo.todoDdlShort == 'Today'"
        >
          <v-divider></v-divider>
        </v-btn>
        <v-card
          v-if="todo.todoDdlShort != 'Today'"
          class="elevation-2"
          style="width:400px;max-width:400px; min-width:400px;border-radius:10px"
        >
          <v-card-title
            :class="todo.todoCheck ? 'green lighten-3' : 'red lighten-2'"
            style="height:50px; color:#fff; font-size:24px; padding:10px 15px 10px 15px; display:flex;"
          >
            <div style="margin-top:-10px">{{ todo.todoName }}</div>
            <v-spacer></v-spacer>
            <v-checkbox
              v-model="todo.todoCheck"
              style="margin-top:0px"
              color="white"
              dense
              @click="checkTodo(index)"
            ></v-checkbox>
          </v-card-title>

          <v-card-text
            class="text--primary white"
            style="padding:15px; text-align:start;"
          >
            {{ todo.todoDetail }}
          </v-card-text>
          <v-card-actions
            style="background-color:#fff"
            @click="gotoProject(todo.projectId)"
          >
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
            <v-spacer></v-spacer>
            <v-btn text color="grey"
              >{{ todo.projectName }}/{{ todo.todolistName }}
              <v-icon>mdi-menu-right</v-icon></v-btn
            >
          </v-card-actions>
        </v-card>
      </v-timeline-item>
    </v-timeline>
  </div>
</template>

<script>
import countDown from "@/components/countDown";
export default {
  data: function() {
    return {
      todos: []
    };
  },
  methods: {
    /** go to correspond project dashboard page */
    gotoProject(id) {
      this.$router.replace({
        path: "/projectdetail/todolist/projectid=" + id
      });
    },
    /** check/uncheck todo method */
    checkTodo(index) {
      this.$axios({
        method: "put",
        url: this.$store.state.host + "todo/check/" + this.todos[index].todoId,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {})
        .catch(error => {
          this.todos[index].todoCheck = !this.todos[index].todoCheck;
          this.$store.commit("response", error);
        });
    }
  },
  created() {
    this.$axios({
      method: "get",
      url:
        this.$store.state.host +
        "todo/get/member/" +
        this.$store.getters.getUsername,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        console.log("123", res.data.data);
        var findToday = false;
        for (var i in res.data.data) {
          if (!findToday) {
            var date = res.data.data[i].todoDdl.split(" ")[0];
            var today =
              new Date().getFullYear() +
              "-" +
              (new Date().getMonth() > 8
                ? new Date().getMonth()
                : "0" + (new Date().getMonth() + 1)) +
              "-" +
              (new Date().getDate() > 9
                ? new Date().getDate()
                : "0" + new Date().getDate());
            if (date > today) {
              this.todos.push({
                todoId: 0,
                todoName: "",
                projectId: 0,
                projectName: "",
                todolistId: 0,
                todolistName: "",
                todoCheck: false,
                todoDdlShort: "Today",
                todoDetail: ""
              });
              findToday = true;
            }
          }
          var todo = res.data.data[i];
          todo.todoDdlShort =
            res.data.data[i].todoDdl.split(" ")[0].split("-")[1] +
            "-" +
            res.data.data[i].todoDdl.split(" ")[0].split("-")[2] +
            " " +
            res.data.data[i].todoDdl.split(" ")[1].split(":")[0] +
            ":" +
            res.data.data[i].todoDdl.split(" ")[1].split(":")[1];
          this.todos.push(todo);
        }
        if (!findToday) {
          this.todos.push({
            todoId: 0,
            todoName: "",
            projectId: 0,
            projectName: "",
            todolistId: 0,
            todolistName: "",
            todoCheck: false,
            todoDdlShort: "Today",
            todoDetail: ""
          });
          console.log(this.todos);
        }
      })
      .catch(error => {
        this.$store.commit("response", error);
      });
  },
  components: { countDown }
};
</script>
