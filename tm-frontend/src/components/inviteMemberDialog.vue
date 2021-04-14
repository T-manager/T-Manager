<template>
  <div>
    <v-btn color="primary" text @click="showUserInviteDialog = true">
      Invite members
      <v-icon>mdi-account-plus-outline</v-icon>
    </v-btn>
    <!-- 点击邀请进入成员dialog -->
    <v-dialog v-model="showUserInviteDialog" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">Invite User</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  outlinedser
                  label="user name"
                  v-model="newMemberName"
                  required
                  hint=""
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <!-- <small>*indicates required field</small> -->
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <!-- 关闭邀请dialog -->
          <v-btn
            depressed
            color="primary"
            text
            @click="showUserInviteDialog = false"
            :loading="loadAddMember"
            :disabled="loadAddMember"
          >
            Close
          </v-btn>
          <!-- 邀请成员并且退出dialog -->
          <v-btn
            depressed
            color="primary"
            text
            @click="addRelation(), (showUserInviteDialog = false)"
            :loading="loadAddMember"
            :disabled="loadAddMember"
          >
            invite
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return { showUserInviteDialog: false, loadAddMember: false };
  },
  methods: {
    addRelation() {
      if (this.project.projectType == "team")
        this.$axios({
          method: "post",
          url: this.$store.state.host + "relation/add",
          data: {
            projectId: this.project.projectId,
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
            //   this.$router.go(0);
          })
          .catch(error => {
            console.log("error");
            console.log(error);
            this.$store.commit("response", error);
            this.loadAddMember = false;
          });
    }
  },
  props: ["project"]
};
</script>
