<template>
  <div>
    <v-btn icon @click="showMember = true" color="primary">
      <v-icon>mdi-account-group</v-icon></v-btn
    >
    <v-dialog v-model="showMember" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">ProjectMembers</span>
          <v-spacer></v-spacer>
          <inviteMemberDialog :project="project"></inviteMemberDialog>
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
                    icons-and-text
                    :color="
                      member.memberRole == 'owner' ? '#f8e71c' : '#7ed321'
                    "
                  >
                    {{ member.memberRole
                    }}<v-icon
                      :color="
                        member.memberRole == 'owner' ? '#f8e71c' : '#7ed321'
                      "
                      style="font-size:35px"
                      >mdi-account</v-icon
                    >
                  </v-btn>
                </div>
              </template>
            </v-list-item>
          </v-list-item-group>
        </v-list>
        <!-- <div
          style="display:flex; justify-content:center; flex-direction:column; align-items:flex-start; padding:30px"
        >
          <div
            v-for="(member, index) in members"
            :key="index"
            style="line-height:60px; font-size:18px; display:flex; width:100%; align-items:center"
          >
            <v-avatar size="40" style="margin-right:10px">
              <v-img
                src="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1526651059,4112790729&fm=15&gp=0.jpg"
              ></v-img>
            </v-avatar>
            {{ member.memberName }}
            <v-spacer></v-spacer>
            <v-icon
              :color="member.memberRole == 'owner' ? '#f8e71c' : '#7ed321'"
              style="font-size:35px"
              >mdi-account</v-icon
            >
          </div>
        </div> -->

        <v-card-actions>
          <v-spacer></v-spacer>
          <!-- 关闭dialog -->
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
          <!-- 保存dialog数据 -->
          <v-btn
            depressed
            color="primary"
            text
            @click="modifyProject(), (showMember = false)"
            :loading="loading"
            :disabled="loading"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import inviteMemberDialog from "@/components/inviteMemberDialog";
export default {
  data: function() {
    return { showMember: false, loading: false, members: [] };
  },
  methods: {
    async modifyProject() {
      this.loading = true;
      console.log(this.project.projectId);
      await this.$axios({
        method: "put",
        url: this.$store.state.host + "project/modify",
        data: this.project,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          console.log(res);
          this.loading = false;
          this.showModifyDialog = false;
          this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
          this.$store.commit("response", error);
          this.loading = false;
        });
    }
  },
  created() {
    this.$axios({
      method: "get",
      url:
        this.$store.state.host + "relation/getuser/" + this.project.projectId,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        console.log("member");
        console.log(res.data.data);
        this.members = res.data.data;
      })
      .catch(error => {
        this.$store.commit("response", error);
      });
  },
  props: ["project"],
  components: { inviteMemberDialog }
};
</script>
