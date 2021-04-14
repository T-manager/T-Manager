<template>
  <v-card max-width="344">
    <v-card @click="gotoProjectDetail" flat>
      <v-img
        src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
        height="200px"
        class="white--text"
        gradient="to bottom, rgba(0,0,0,0), rgba(100,100,100, 0.5)"
      >
        <v-card-title
          style="font-size: 1.5em; width:100%; height:100%; display:flex; justify-content:flex-end; align-items:flex-end"
          >{{ project.projectType }}</v-card-title
        ></v-img
      >
    </v-card>
    <div
      style="display:flex; align-items:center; padding-left:20px; padding-right:20px; padding-top:10px"
    >
      <div style="font-size:25px">{{ project.projectName }}</div>
      <v-spacer></v-spacer>
      <!-- 点击铅笔修改项目 -->
      <modifyProjectDialog :project="project"></modifyProjectDialog>
      <v-btn icon color="primary" @click="deleteProject()">
        <v-icon>mdi-delete-outline</v-icon></v-btn
      >
    </div>
    <div
      style="display:flex; justify-content:flex-end; padding-left:20px; padding-right:20px; padding-bottom:10px; padding-top:10px"
    >
      <memberDialog
        v-if="project.projectType == 'team'"
        :project="project"
      ></memberDialog>
      <v-spacer></v-spacer>
      <v-btn color="primary" icon @click="showDetail = !showDetail">
        <v-icon>{{
          showDetail ? "mdi-chevron-up" : "mdi-chevron-down"
        }}</v-icon>
      </v-btn>
    </div>
    <v-expand-transition>
      <div v-show="showDetail">
        <v-divider></v-divider>
        <v-card-text
          style="width:100%; display:flex; justify-content:flex-start"
          >{{ project.projectDetail }}
        </v-card-text>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script>
import modifyProjectDialog from "@/components/modifyProjectDialog";
import memberDialog from "@/components/memberDialog";
export default {
  data: function() {
    return {
      dialog: false,

      showDetail: true,
      show: true,
      loading: {
        delete: false,
        modify: false
      },
      showModifyDialog: false
    };
  },
  methods: {
    gotoProjectDetail() {
      this.$router.replace({
        path: "/projectdetail/todolist/projectid=" + this.project.projectId
      });
    },
    async modifyProject() {
      this.loading.modify = true;
      console.log(this.project.projectId);
      await this.$axios({
        method: "put",
        url: this.$store.state.host + "project/modify",
        data: {
          projectId: this.project.projectId,
          projectName: this.project.projectName,
          projectDetail: this.project.projectDetail,
          projectOwner: this.project.projectOwner
          //projectType: this.newProjectType
        }
      })
        .then(res => {
          console.log(res);
          this.loading.modify = false;
          this.showModifyDialog = false;
          this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
          this.$store.commit("response", error);
          this.loading.modify = false;
        });
    },
    deleteProject() {
      this.$axios({
        method: "delete",
        url:
          this.$store.state.host + "project/delete/" + this.project.projectId,
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
  components: {
    modifyProjectDialog,
    memberDialog
  },
  props: ["project"]
};
</script>
