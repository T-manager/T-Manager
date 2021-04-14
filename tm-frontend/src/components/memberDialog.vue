<template>
  <div>
    <v-btn icon @click="showMember = true" color="primary">
      <v-icon>mdi-account-group</v-icon></v-btn
    >
    <v-dialog v-model="showMember" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">ProjectMembers</span>
        </v-card-title>
        <v-card-text>
          <div v-for="(member, index) in members" :key="index">
            {{ member.memberName }} {{ member.memberRole }}
          </div>
        </v-card-text>

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
  props: ["project"]
};
</script>
