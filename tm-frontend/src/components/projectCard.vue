<template>
  <v-card width="400px" :style="showDetail ? '' : 'height:256px'">
    <v-card
      @click="gotoProjectDetail"
      style="border-bottom-left-radius:0px; border-bottom-right-radius:0px"
      flat
    >
      <v-img
        :src="
          project.projectType == 'team'
            ? 'https://cdn.vuetifyjs.com/images/cards/sunshine.jpg'
            : 'https://cdn.vuetifyjs.com/images/cards/cooking.png'
        "
        height="200px"
        class="white--text"
        gradient="to bottom, rgba(0,0,0,0), rgba(100,100,100, 0.5)"
      >
        <div
          style="font-size:25px; height:200px; display:flex; flex-direction:column; justify-content:space-between;
          align-items:flex-end; padding:10px"
        >
          <v-menu offset-x :close-on-content-click="false">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                v-bind="attrs"
                v-on="on"
                small
                style="width:40px; height:38px"
              >
                <v-icon style="font-size:30px; color:#757575"
                  >mdi-dots-vertical</v-icon
                >
              </v-btn>
            </template>
            <v-list style="padding:0px;">
              <v-list-item
                style="padding:0px 5px 0px 5px"
                v-if="project.projectOwner == $store.getters.getUsername"
              >
                <!-- 点击铅笔修改项目 -->
                <modifyProjectDialog :project="project"></modifyProjectDialog>
              </v-list-item>
              <v-list-item
                style="padding:0px 5px 0px 5px"
                v-if="project.projectType == 'team'"
              >
                <memberDialog :project="project"></memberDialog
              ></v-list-item>
              <v-list-item
                style="padding:0px 5px 0px 5px"
                v-if="project.projectOwner == $store.getters.getUsername"
              >
                <!-- 解散project -->
                <v-btn
                  text
                  color="primary"
                  @click="showPopupMethod"
                  style="width:120px; display:flex; justify-content:flex-start; padding:0px 10px 0px 10px"
                >
                  Disband
                  <v-spacer></v-spacer>
                  <v-icon>mdi-account-multiple-remove-outline</v-icon></v-btn
                >
              </v-list-item>
              <!-- 退出project -->
              <v-list-item style="padding:0px 5px 0px 5px" v-else>
                <v-btn
                  text
                  color="primary"
                  @click="quitProject()"
                  style="width:120px; display:flex; justify-content:flex-start; padding:0px 10px 0px 10px"
                >
                  Quit
                  <v-spacer></v-spacer>
                  <v-icon>mdi-account-remove-outline</v-icon></v-btn
                >
              </v-list-item>
            </v-list>
          </v-menu>
          <div>{{ project.projectType }}</div>
        </div>
      </v-img>
    </v-card>
    <div
      style="display:flex; align-items:center; justify-content:flex-end; padding-left:20px; padding-right:20px; padding-bottom:10px; padding-top:10px;"
    >
      <div
        :style="
          project.projectName.length > 13
            ? ';font-size:16px'
            : ';font-size:20px'
        "
      >
        {{ project.projectName }}
      </div>
      <v-spacer></v-spacer>
      <v-btn style="color:#757575" icon @click="showDetail = !showDetail">
        <v-icon>{{
          showDetail ? "mdi-chevron-up" : "mdi-chevron-down"
        }}</v-icon>
      </v-btn>
    </div>
    <v-expand-transition>
      <div v-if="showDetail">
        <v-divider></v-divider>
        <v-card-text class="Omit" style="max-width:400px;text-align:start"
          >{{ project.projectDetail }}
        </v-card-text>
      </div>
    </v-expand-transition>
    <popup
      :message="
        'Are you sure you want to ' +
          (project.projectOwner == $store.getters.getUsername
            ? 'disband'
            : 'quit') +
          ' this project team?'
      "
      :showPopup="showPopup"
      @showPopupMethod="showPopupMethod"
      @confirmOperation="
        project.projectOwner == $store.getters.getUsername
          ? deleteProject()
          : quitProject()
      "
    ></popup>
  </v-card>
</template>

<script>
import modifyProjectDialog from "@/components/modifyProjectDialog";
import memberDialog from "@/components/memberDialog";
import popup from "@/components/popup";
export default {
  data: function() {
    return {
      dialog: false,
      showPopup: false,
      showDetail: false,
      show: true,
      loading: {
        delete: false,
        modify: false
      },
      showModifyDialog: false
    };
  },
  methods: {
    showPopupMethod() {
      this.showPopup = !this.showPopup;
    },
    quitProject() {
      console.log("quit");
      this.$axios({
        method: "delete",
        url:
          this.$store.state.host +
          "relation/delete/" +
          this.project.projectMemberId,
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
    },
    gotoProjectDetail() {
      this.$router.replace({
        path: "/projectdetail/todolist/projectid=" + this.project.projectId
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
    memberDialog,
    popup
  },
  props: ["project"]
};
</script>

<style>
.Omit {
  display: block;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
