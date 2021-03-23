<template>
  <div>
    <v-btn icon @click="showModifyTodolist = true" color="primary">
      <v-icon>mdi-pencil-outline</v-icon></v-btn
    >
    <v-dialog v-model="showModifyTodolist" persistent max-width="600px">
      <v-card class="card">
        <v-card-title>
          <span class="headline">Modify todolist</span>
        </v-card-title>
        <v-card-text>
          <v-text-field
            label="todo list name"
            color="primary"
            prepend-icon="mdi-leaf"
            v-model="todolist.todolistName"
          ></v-text-field>
        </v-card-text>
        <div class="card_action">
          <v-btn
            color="indigo lighten-5"
            class="indigo--text"
            @click="showModifyTodolist = false"
          >
            <v-icon class="pr-2">mdi-cancel</v-icon>Cancel
          </v-btn>
          <v-btn
            color="primary"
            style="color:#fff; margin-left:20px"
            @click="modifyTodolist()"
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
      showModifyTodolist: false,

      rules: {
        nameRules: [
          v =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 3) ||
            "the length of name should be 3-20"
        ]
      }
    };
  },
  props: ["todolist"],
  methods: {
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 3;
    },
    checkRules() {
      if (!this.checkNameRules(this.todolist.todolistName)) {
        alert("check the name");
        return false;
      }
      return true;
    },
    async modifyTodolist() {
      this.loading = true;
      await this.$axios({
        method: "put",
        url: this.$store.state.host + "todolist/modify",
        data: this.todolist
      })
        .then(res => {
          console.log(res);
          this.loading = false;
          this.showModifyTodolist = false;
          //   this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
          //   this.$store.commit("response", error);
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
