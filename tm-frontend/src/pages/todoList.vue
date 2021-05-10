<template>
  <div style="display:flex; padding: 0px 58px 5px 58px; flex-direction:column">
    <v-text-field
      solo
      append-icon="mdi-magnify"
      style="border-radius: 30px; height: 45px; width:400px; margin-left: 990px;"
      maxlength="20"
      @keyup.enter="search"
      @click:append="search"
      color="primary"
      label="Search Todo"
      v-model="searchText"
    >
    </v-text-field>
    <div
      v-if="!showTodoLists"
      style="width:100%; display:flex; justify-content:center; margin-top:200px; align-items:center"
    >
      <div style="colos:#434343; font-size:28px">Loading...</div>
      <v-btn x-large icon :loading="!showTodoLists"></v-btn>
    </div>

    <div v-if="showTodoLists" style="overflow-x:auto; display:flex">
      <div v-for="(todolist, index) in todolists" :key="index">
        <!-- A todolist card -->
        <todolistCard
          :projectId="projectId"
          :todolist="todolist"
        ></todolistCard>
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
  </div>
</template>

<script>
import todolistCard from "@/components/todolistCard";
export default {
  data: function() {
    return {
      showAddTodolist: false,
      showTodoLists: false,
      loadAddTodoList: false,
      newTodolistName: "",
      projectId: 0,
      todolists: "",
      todolist: {},
      searchText: ""
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
    },
    /** search todo */
    search() {
      this.showTodoLists = false;
      if (this.searchText == "") this.getTodolists();
      else {
        this.$axios({
          method: "get",
          url:
            this.$store.state.host +
            "todo/get/" +
            this.projectId +
            "/search/" +
            this.searchText,
          headers: {
            Authorization: "Bearer " + this.$store.getters.getToken
          }
        })
          .then(res => {
            this.todolists = res.data.data;
            this.showTodoLists = true;
          })
          .catch(error => {
            this.$store.commit("response", error);
          });
      }
    },
    /**get all todolist belongs to the project*/
    getTodolists() {
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
          this.showTodoLists = true;
        })
        .catch(error => {
          this.$store.commit("response", error);
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
    this.getTodolists();
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
