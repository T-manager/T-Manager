<template>
  <div style="display: flex; flex-direction: column; align-items: center">
    <div
      style="
        display: flex;
        flex-direction: column;
        padding: 35px;
        width: 1350px;
        justify-content: flex-start;
      "
    >
      <v-row style="margin-top: 20px">
        <v-text-field
          solo
          prepend-inner-icon="mdi-magnify"
          style="
            border-top-left-radius: 30px;
            border-bottom-left-radius: 30px;
            height: 50px;
            margin-left: 830px;
          "
          color="primary"
          label="Search Project"
          v-model="search"
        >
        </v-text-field>
        <v-menu offset-x>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              color="white"
              style="
                min-width: 20px;
                height: 48px;
                margin-left: 3px;
                margin-right: 30px;
                border-top-right-radius: 30px;
                border-bottom-right-radius: 30px;
              "
            >
              <v-icon color="primary">mdi-filter-outline</v-icon>
            </v-btn>
          </template>
          <v-list style="padding: 0px">
            <v-list-item @click="showProjectType = 'All'">
              <v-list-item-title>All</v-list-item-title>
            </v-list-item>
            <v-list-item @click="showProjectType = 'team'">
              <v-list-item-title>Team</v-list-item-title>
            </v-list-item>
            <v-list-item @click="showProjectType = 'personal'">
              <v-list-item-title>Personal</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-row>
      <v-row style="margin-top: 40px">
        <!-- Card for all the projects that have been created -->
        <projectCard
          style="margin: 15px"
          v-for="(project, index) in projects"
          :key="index"
          v-if="
            showProjects &&
            (showProjectType == project.projectType || showProjectType == 'All')
          "
          :project="project"
        ></projectCard>
        <!--Dialog for creating a new project-->
        <v-dialog v-model="dialog" persistent max-width="550px">
          <!-- Plus icon to create a new project -->
          <template v-slot:activator="{ on, attrs }">
            <v-card class="plusProject">
              <v-card-title>Click to create new project</v-card-title>
              <v-btn icon color="primary" dark v-bind="attrs" v-on="on">
                <v-icon large>mdi-plus</v-icon>
              </v-btn>
            </v-card>
          </template>
          <v-card style="padding: 30px 35px 50px 35px" class="card-background">
            <div
              style="
                font-size: 30px;
                margin-left: 10px;
                width: 100%;
                text-align: left;
              "
            >
              <v-img></v-img>
              Create Project
            </div>
            <v-card-text style="margin-top: 30px; padding: 10px">
              <v-text-field
                outlined
                label="project name"
                v-model="newProject.projectName"
                required
                :rules="rules.nameRules"
                counter
                hint="more than 1 and less than 20"
              ></v-text-field>
              <v-textarea
                outlined
                label="project detail"
                v-model="newProject.projectDetail"
                required
                counter
                auto-grow
                rows="1"
                :rules="rules.detailRules"
                hint="less than 100"
              ></v-textarea>
              <v-autocomplete
                outlined
                color="primary"
                :items="['personal', 'team']"
                label="project type"
                v-model="newProject.projectType"
                :rules="rules.selectRules"
              ></v-autocomplete>
            </v-card-text>

            <div
              style="display: flex; justify-content: center; margin-top: 10px"
            >
              <!-- Close the dialog -->
              <v-btn
                depressed
                style="border: #cccccc solid 1px; color: #777777; width: 100px"
                @click="dialog = false"
                :loading="loadAddProject"
                :disabled="loadAddProject"
              >
                Cancel
              </v-btn>
              <!-- Button to save dialog data -->
              <v-btn
                depressed
                color="primary"
                style="color: #fff; width: 100px; margin-left: 50px"
                @click="addProject()"
                :loading="loadAddProject"
                :disabled="loadAddProject"
              >
                Submit
              </v-btn>
            </div>
          </v-card>
        </v-dialog>
      </v-row>
    </div>
  </div>
</template>

<script>
import projectCard from "@/components/projectCard";
export default {
  data: function () {
    return {
      dialog: false,
      showProjectType: "All",
      rules: {
        nameRules: [
          (v) =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 1) ||
            "the length of name should be 1-20",
        ],
        detailRules: [
          (v) =>
            (typeof v != "undefined" && v.length <= 100) ||
            "the length of detail should less than 100",
        ],
        selectRules: [(v) => !!v || "please choose a type"],
      },
      showProjects: false,
      loadAddProject: false,
      loadAddMember: false,
      projects: [],
      newProject: {
        projectName: "",
        projectDetail: "",
        projectOwner: this.$store.getters.getUsername,
        projectType: "",
      },
      memberItem: 1,
      items: [
        { text: "ProjectOwmer", icon: "mdi-account" },
        { text: "member1", icon: "mdi-account" },
        { text: "member2", icon: "mdi-account" },
      ],
    };
  },
  methods: {
    showAll() {
      showProjectType = "All";
    },
    showPersonal() {
      showProjectType = "Personal";
    },
    showTeam() {
      showProjectType = "Team";
    },
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 1;
    },
    checkDetailRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 100;
    },
    checkSelectRule(v) {
      if (typeof v == "undefined" || v == "") return false;
      return true;
    },
    /**Form validation*/
    checkRules() {
      if (!this.checkNameRules(this.newProject.projectName)) {
        alert("check the name");
        return false;
      }
      if (!this.checkDetailRules(this.newProject.projectDetail)) {
        alert("check the detail");
        return false;
      }
      if (!this.checkSelectRule(this.newProject.projectType)) {
        alert("check the type");
        return false;
      }

      return true;
    },
    /**create a project*/
    addProject() {
      this.loadAddProject = true;
      if (!this.checkRules()) {
        this.loadAddProject = false;
        return;
      }
      this.$axios({
        method: "post",
        url: this.$store.state.host + "project/add",
        data: this.newProject,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken,
        },
      })
        .then((res) => {
          this.loadAddProject = false;
          this.$router.go(0);
        })
        .catch((error) => {
          this.$store.commit("response", error);
          this.loadAddProject = false;
        });
    },
  },
  created() {
    /**check if the user has logged in*/
    if (this.$store.getters.getToken == null) {
      alert("You are not signned in yet!");
      var path = "/login";
      this.$router.push({ path: path });
    }
    /**get all projects belongs to the user*/
    this.$axios({
      method: "get",
      url:
        this.$store.state.host +
        "relation/getproject/" +
        this.$store.getters.getUsername,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken,
      },
    })
      .then((res) => {
        this.projects = res.data.data;
        this.showProjects = true;
      })
      .catch((error) => {
        // console.log(error);
        this.$store.commit("response", error);
      });
  },
  components: {
    projectCard,
  },
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
.card-background {
  background-image: url("../assets/TmanagerLogo_l5.svg");
  /* background-image: url("../assets/TManagerLogo.png"); */
  background-size: 520px;
  background-repeat: no-repeat;
  background-position: 140px -65px;
}
</style>
