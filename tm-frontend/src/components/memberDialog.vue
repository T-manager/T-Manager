<template>
  <div>
    <!-- shou member button -->
    <v-btn
      text
      style="width:120px; display:flex; justify-content:flex-start; padding:0px 10px 0px 10px"
      @click="showMember = true"
      color="primary"
    >
      Member
      <v-spacer></v-spacer>
      <v-icon>mdi-account-group</v-icon></v-btn
    >
    <!-- show member popup -->
    <v-dialog v-model="showMember" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">ProjectMembers</span>
          <v-spacer></v-spacer>
          <inviteMemberDialog
            v-if="project.projectOwner == $store.getters.getUsername"
            :project="project"
          ></inviteMemberDialog>
        </v-card-title>
        <v-list>
          <v-list-item-group multiple>
            <v-list-item
              @click="showTodoDetail = true"
              v-for="(member, index) in members"
              :key="index"
            >
              <template>
                <div
                  style="display:flex; justify-content:flex-start; align-items:center; margin-left:10px; width:100%"
                >
                  <v-avatar size="40" style="margin-right:10px">
                    <v-img
                      src="https://img2.baidu.com/it/u=2781655315,1484440222&fm=11&fmt=auto&gp=0.jpg"
                    ></v-img>
                  </v-avatar>
                  {{ member.memberName }}
                  <v-spacer></v-spacer>
                  <v-btn
                    v-if="member.memberRole == 'owner'"
                    text
                    style="color:#f8e71c; width:140px"
                    v-bind="attrs"
                    v-on="on"
                  >
                    {{ member.memberRole }}<v-spacer></v-spacer
                    ><v-icon color="#f8e71c" style="font-size:35px"
                      >mdi-account-star-outline</v-icon
                    >
                  </v-btn>
                  <v-menu v-else offset-x style="min-width:50px">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        text
                        style="color:#7ed321; width:140px"
                        v-bind="attrs"
                        v-on="on"
                        :disabled="
                          project.projectOwner != $store.getters.getUsername
                        "
                      >
                        {{ member.memberRole }}<v-spacer></v-spacer
                        ><v-icon color="#7ed321" style="font-size:35px"
                          >mdi-account</v-icon
                        >
                      </v-btn>
                    </template>
                    <v-list style="padding:0px;">
                      <v-list-item @click="showPopupMethod">
                        <v-list-item-title>Remove</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </div>
              </template>
              <popup
                message="Are you sure you want to remove this member?"
                :showPopup="showPopup"
                @showPopupMethod="showPopupMethod"
                @confirmOperation="deleteRelation(member.projectMemberId)"
              ></popup>
            </v-list-item>
          </v-list-item-group>
        </v-list>

        <v-card-actions>
          <v-spacer></v-spacer>
          <!-- close popup -->
          <v-btn
            depressed
            color="primary"
            text
            @click="showMember = false"
            :loading="loading"
            :disabled="loading"
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import inviteMemberDialog from "@/components/inviteMemberDialog";
import popup from "@/components/popup";
export default {
  data: function() {
    return { showMember: false, showPopup: false, loading: false, members: [] };
  },
  methods: {
    /** show popup method
     * provide for child component to change the showing state
     */
    showPopupMethod() {
      this.showPopup = !this.showPopup;
    },
    async modifyProject() {
      this.loading = true;
      await this.$axios({
        method: "put",
        url: this.$store.state.host + "project/modify",
        data: this.project,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.loading = false;
          this.showModifyDialog = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loading = false;
        });
    },
    /** delete relation method */
    deleteRelation(memberId) {
      this.$axios({
        method: "delete",
        url: this.$store.state.host + "relation/delete/" + memberId,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          this.showMenber = false;
          this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    }
  },
  created() {
    // get all members of the project
    this.$axios({
      method: "get",
      url:
        this.$store.state.host + "relation/getuser/" + this.project.projectId,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        this.members = res.data.data;
      })
      .catch(error => {
        this.$store.commit("response", error);
      });
  },
  props: ["project"],
  components: { inviteMemberDialog, popup }
};
</script>
