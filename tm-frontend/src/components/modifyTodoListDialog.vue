<template>
  <div>
    <!-- modify todo button -->
    <v-btn icon @click="showModifyTodolist = true" color="primary">
      <v-icon>mdi-pencil-outline</v-icon></v-btn
    >
    <!-- modify todo popup -->
    <v-dialog v-model="showModifyTodolist" persistent max-width="550px">
      <v-card style="padding: 30px 35px 50px 35px;" class="card-background">
        <div
          style="font-size:30px; margin-left:10px; width:100%; text-align:left"
        >
          Modify Todolist
        </div>
        <v-card-text style="margin-top:30px; padding:10px">
          <v-text-field
            label="todo list name"
            color="primary"
            prepend-icon="mdi-leaf"
            v-model="todolist.todolistName"
            hint="more than 1 and less than 20"
            :rules="rules.nameRules"
          ></v-text-field>
        </v-card-text>
        <div class="card_action">
          <!-- close popup -->
          <v-btn
            depressed
            style="border:#cccccc solid 1px; color:#777777; width:120px; margin-right:50px; "
            @click="showModifyTodolist = false"
          >
            <v-icon class="pr-2">mdi-cancel</v-icon>Cancel
          </v-btn>
          <!-- submit modify -->
          <v-btn
            depressed
            color="primary"
            style="color:#fff; width:120px"
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
      // inline check rules
      rules: {
        nameRules: [
          v =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 1) ||
            "the length of name should be 1-20"
        ]
      }
    };
  },
  props: ["todolist"],
  methods: {
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 1;
    },
    /** check all rules */
    checkRules() {
      if (!this.checkNameRules(this.todolist.todolistName)) {
        alert("check the name");
        return false;
      }
      return true;
    },
    /** modify todo list method */
    async modifyTodolist() {
      this.loading = true;
      if (!this.checkRules()) {
        // if not satisfy rules, reject
        this.loading = false;
        return;
      }
      await this.$axios({
        method: "put",
        url: this.$store.state.host + "todolist/modify",
        data: this.todolist,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.loading = false;
          this.showModifyTodolist = false;
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
