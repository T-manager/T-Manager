<template>
  <div style="display:flex; padding: 0px 58px 5px 58px; overflow-x:auto;">
    <div v-for="(todolist, index) in todolists" :key="index">
      <!-- A todolist card -->
      <todolistCard :todolist="todolist"></todolistCard>
    </div>
    <!-- A button to add todolist -->
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
      <!-- Close the dialog -->
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
      projectId: 0,
      todolists: "",
      todolist: {}
    };
  },
  methods: {
    /**Controls whether to display the dialog that added Todolist */
    showAddNewTodolist() {
      if (this.showAddTodolist == false) this.showAddTodolist = true;
    },
    /**Hide todolist*/
    hideAddNewTodolist() {
      this.showAddTodolist = false;
    },
    /**create a todolist*/
    addTodolist() {
      this.loadAddTodoList = true;
      this.$axios({
        method: "post",
        url: this.$store.state.host + "todolist/add",
        data: {
          projectId: this.projectId,
          todolistName: this.newTodolistName
        },
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
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
    /**check if the user has logged in*/
    if (this.$store.getters.getToken == null) {
      alert("You are not signned in yet!");
      var path = "/login";
      this.$router.push({ path: path });
    }
    this.projectId = this.$route.path.split("projectid=")[1];
    /**get all todolist belongs to the project*/
    this.$axios({
      method: "get",
      url: this.$store.state.host + "todolist/get/" + this.projectId,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        // this.todolists = res.data.data.reverse();
        this.todolists = res.data.data;
        for (var i in this.todolist.todoViewDTO) {
          this.todolist.todiViewDTO[i].loading = false;
        }
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
  min-width: 400px;
  max-width: 400px;
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
