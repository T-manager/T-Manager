<template>
  <div v-if="show" style="margin:15px">
    <!-- todolist card -->
    <v-card class="todolist" style="border-radius:10px">
      <div style="display:flex; padding: 0px 25px 0px 25px;">
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
          <modifyTodoListDialog :todolist="todolist"></modifyTodoListDialog>
          <v-btn
            icon
            @click="showPopupMethod"
            :loading="loading.delete"
            :disabled="loading.delete"
          >
            <v-icon>mdi-close</v-icon></v-btn
          >
        </div>
      </div>

      <v-list flat>
        <!-- <v-subheader>All</v-subheader> -->
        <v-list-item-group multiple>
          <!-- 每个TODO -->
          <div style="max-height:490px; overflow-y:auto">
            <div v-for="(todo, index) in todolist.todoViewDTO" :key="index">
              <todoDetailDialog
                @changeCompleteNum="changeCompleteNum(index)"
                :todolistName="todolist.todolistName"
                :todo="todo"
                :projectName="todolist.projectName"
              ></todoDetailDialog>
            </div>
          </div>
          <!-- add new todo -->
          <addTodoDialog :todolist="todolist"></addTodoDialog>
        </v-list-item-group>
      </v-list>
    </v-card>
    <popup
      message="Are you sure you want to delete this todo list?"
      :showPopup="showPopup"
      @showPopupMethod="showPopupMethod"
      @confirmOperation="deleteTodolist"
    ></popup>
  </div>
</template>

<script>
import addTodoDialog from "@/components/addTodoDialog";
import modifyTodoListDialog from "@/components/modifyTodoListDialog";
import todoDetailDialog from "@/components/todoDetailDialog";
import popup from "@/components/popup";
export default {
  data: function() {
    return {
      show: true,
      showPopup: false,
      loading: {
        delete: false
      }
    };
  },
  methods: {
    showPopupMethod() {
      this.showPopup = !this.showPopup;
    },
    changeCompleteNum(index) {
      if (this.todolist.todoViewDTO[index].todoCheck == false)
        this.todolist.todolistCompleteNum--;
      else this.todolist.todolistCompleteNum++;
    },
    deleteTodolist() {
      this.loadAddTodoList = true;
      this.$axios({
        method: "delete",
        url:
          this.$store.state.host +
          "todolist/delete/" +
          this.todolist.todolistId,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.show = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    }
  },
  props: ["todolist"],
  components: {
    addTodoDialog,
    modifyTodoListDialog,
    todoDetailDialog,
    popup
  }
};
</script>

<style>
.todolist {
  border-radius: 10px;
  width: 350px;
  padding: 25px 0px 10px 0px;
}
.todolist_info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
