<template>
  <div>
    <!-- modify project button -->
    <v-btn
      text
      @click="showModifyProject = true"
      color="primary"
      style="
        width: 120px;
        display: flex;
        justify-content: flex-start;
        padding: 0px 10px 0px 10px;
      "
    >
      Edit
      <v-spacer></v-spacer>
      <v-icon>mdi-pencil-outline</v-icon></v-btn
    >
    <!-- modify project popup -->
    <v-dialog v-model="showModifyProject" persistent max-width="600px">
      <v-card style="padding: 30px 35px 50px 35px;" class="card-background">
        <div
          style="font-size:30px; margin-left:10px; width:100%; text-align:left"
        >
          Modify Project
        </div>
        <v-card-text style="margin-top:30px; padding:10px">
          <v-text-field
            outlined
            label="project name"
            v-model="project.projectName"
            required
            :rules="rules.nameRules"
            hint="more than 1 and less than 20"
          ></v-text-field>
          <v-textarea
            outlined
            label="project detail"
            v-model="project.projectDetail"
            required
            auto-grow
            rows="1"
            :rules="rules.detailRules"
            hint="less than 100"
          ></v-textarea>
        </v-card-text>

        <div style="display:flex; justify-content:center; margin-top:10px">
          <!-- close popup -->
          <v-btn
            depressed
            style="border:#cccccc solid 1px; color:#777777; width:100px"
            @click="showModifyProject = false"
            :loading="loading"
            :disabled="loading"
          >
            Cancel
          </v-btn>
          <!-- submit modify -->
          <v-btn
            depressed
            color="primary"
            style="color:#fff; width:100px; margin-left:50px"
            @click="modifyProject()"
            :loading="loading"
            :disabled="loading"
          >
            Submit
          </v-btn>
        </div>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      showModifyProject: false,
      loading: false,
      // inline check rules
      rules: {
        nameRules: [
          v =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 1) ||
            "the length of name should be 1-20"
        ],
        detailRules: [
          v =>
            (typeof v != "undefined" && v.length <= 100) ||
            "the length of detail should less than 100"
        ]
      }
    };
  },
  methods: {
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 1;
    },
    checkDetailRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 100;
    },
    /** check all rules beform submit again */
    checkRules() {
      if (!this.checkNameRules(this.project.projectName)) {
        alert("check the name");
        return false;
      }
      if (!this.checkDetailRules(this.project.projectDetail)) {
        alert("check the detail");
        return false;
      }
      return true;
    },
    /** modify project submission */
    async modifyProject() {
      this.loading = true;
      if (!this.checkRules()) {
        this.loading = false;
        return;
      }
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
    }
  },
  props: ["project"]
};
</script>

<style scoped>
.card-background {
  background-image: url("../assets/TmanagerLogo_l5.svg");
  background-size: 520px;
  background-repeat: no-repeat;
  background-position: 140px -65px;
}
</style>
