<template>
  <div style="display: flex; padding: 35px">
    <v-row>
      <!-- 已经创建的的所有项目CARD -->
      222
      <div
        v-for="(project, index) in projects"
        :key="index"
        v-if="showProjects"
      >
        111
        <projectCard :project="project"></projectCard>
      </div>
      333
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
      showUserInviteDialog: false,
      showModifyDialog: false,
      showDetail: true,
      showProjects: false,
      show: true,
      loadAddProject: false,
      loadAddMember: false,
      loading: {
        delete: false,
        modify: false
      },
      projects: [],
      newProject: {
        projectName: "",
        projectDetail: "",
        projectOwner: this.$store.getters.getUsername,
        projectType: ""
      },
      project: {
        projectId: 1,
        projectName: 1,
        projectDetail: 114514,
        projectOwner: 1
      },
      relation: {
        relationId: 1,
        projectId: 1111,
        memberName: "田所浩二"
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
    // showProjectType(){
    //   if(this.project.)
    // },
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
    },
    async modifyProject() {
      this.loading = true;
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
          this.loading = false;
          this.showModifyDialog = false;
          //   this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
          this.$store.commit("response", error);
          this.loading = false;
        });
    },
    deleteProject() {
      // this.loadAddProject = true;
      this.$axios({
        method: "delete",
        url: this.$store.state.host + "project/delete/" + this.project.projectId
      })
        .then(res => {
          this.show = false;
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    },
    addRelation() {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "relation/add",
        data: {
          projectId: this.projectId,
          memberName: this.newMemberName
        },
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.showMenber = true;
          console.log(res);
          this.loadAddMember = false;
          //this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loadAddMember = false;
        });
    },
    deleteRelation() {
      this.$axios({
        method: "delete",
        url:
          this.$store.state.host +
          "relation/delete/" +
          this.relation.relationId,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.showMenber = false;
        })
        .catch(error => {
          this.$store.commit("response", error);
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
        this.project = res.data.data;
        console.log(this.project);
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
