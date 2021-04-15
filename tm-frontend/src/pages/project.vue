<template>
  <div style="display: flex; padding: 35px">
    <v-row>
      <!-- 已经创建的的所有项目CARD -->
      <projectCard
        style="margin:15px;"
        v-for="(project, index) in projects"
        :key="index"
        v-if="showProjects"
        :project="project"
      ></projectCard>
      <!--新建项目dialog-->
      <v-dialog v-model="dialog" persistent max-width="600px">
        <!-- 点击加号新建项目 -->
        <template v-slot:activator="{ on, attrs }">
          <v-card class="plusProject">
            <v-card-title>Click to create new project</v-card-title>
            <v-btn icon color="primary" dark v-bind="attrs" v-on="on">
              <v-icon large>mdi-plus</v-icon>
            </v-btn>
          </v-card>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">Create Project</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    outlined
                    label="project name"
                    v-model="newProject.projectName"
                    required
                    hint="less than 20"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    outlined
                    label="project detail"
                    v-model="newProject.projectDetail"
                    required
                    hint="more than 1 and less than 100"
                  ></v-text-field>
                </v-col>
                <!-- <v-col cols="12">
                  <v-text-field
                    outlined
                    label="user name"
                    v-model="newProjectOwner"
                    required
                    hint="you user name"
                  ></v-text-field>
                </v-col> -->
                <!-- 待定 -->
                <v-col cols="12">
                  <v-autocomplete
                    :items="['personal', 'team']"
                    label="project type"
                    v-model="newProject.projectType"
                  ></v-autocomplete>
                </v-col>
              </v-row>
            </v-container>
            <!-- <small>*indicates required field</small> -->
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <!-- 关闭dialog -->
            <v-btn
              depressed
              color="primary"
              text
              @click="dialog = false"
              :loading="loadAddProject"
              :disabled="loadAddProject"
            >
              Close
            </v-btn>
            <!-- 保存dialog数据 -->
            <v-btn
              depressed
              color="primary"
              text
              @click="addProject(), (dialog = false)"
              :loading="loadAddProject"
              :disabled="loadAddProject"
            >
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>

<script>
import projectCard from "@/components/projectCard";
export default {
  data: function() {
    return {
      dialog: false,

      showProjects: false,
      loadAddProject: false,
      loadAddMember: false,
      projects: [],
      newProject: {
        projectName: "",
        projectDetail: "",
        projectOwner: this.$store.getters.getUsername,
        projectType: ""
      },
      memberItem: 1,
      items: [
        { text: "ProjectOwmer", icon: "mdi-account" },
        { text: "member1", icon: "mdi-account" },
        { text: "member2", icon: "mdi-account" }
      ]
    };
  },
  methods: {
    addProject() {
      this.loadAddProject = true;
      this.$axios({
        method: "post",
        url: this.$store.state.host + "project/add",
        data: this.newProject,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          console.log(res);
          this.loadAddProject = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loadAddProject = false;
        });
    }
  },
  created() {
    this.$axios({
      method: "get",
      url:
        this.$store.state.host +
        "relation/getproject/" +
        this.$store.getters.getUsername,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        console.log(res);
        // projects
        this.projects = res.data.data;
        console.log(this.projects);
        this.showProjects = true;
      })
      .catch(error => {
        console.log(error);
        this.$store.commit("response", error);
      });
  },
  //props: ["project"],
  components: {
    projectCard
  }
};
</script>
<style scoped>
.plusProject {
  margin: 15px;
  width: 400px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0px 10px 0px 10px;
}
.v-card--reveal {
  bottom: 0;
  opacity: 1 !important;
  position: absolute;
  width: 100%;
}
</style>
