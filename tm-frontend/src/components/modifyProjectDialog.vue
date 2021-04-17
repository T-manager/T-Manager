<template>
  <div>
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
    <v-dialog v-model="showModifyProject" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">Modify Project</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  outlined
                  label="project name"
                  v-model="project.projectName"
                  required
                  :rules="rules.nameRules"
                  hint="more than 1 and less than 20"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  outlined
                  label="project detail"
                  v-model="project.projectDetail"
                  required
                  :rules="rules.detailRules"
                  hint="less than 100"
                ></v-text-field>
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
            @click="showModifyProject = false"
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
            @click="modifyProject()"
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
    return {
      showModifyProject: false,
      loading: false,
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
          console.log(res);
          this.loading = false;
          this.showModifyDialog = false;
          console.log("project");
          console.log(this.project);
          this.$router.go(0);
        })
        .catch(error => {
          console.log("project");
          console.log(this.project);
          console.log(error);
          this.$store.commit("response", error);
          this.loading = false;
        });
    }
  },
  props: ["project"]
};
</script>
