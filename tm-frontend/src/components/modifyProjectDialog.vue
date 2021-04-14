<template>
  <div>
    <v-btn icon @click="showModifyProject = true" color="primary">
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
                  hint="less than 20"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  outlined
                  label="project detail"
                  v-model="project.projectDetail"
                  required
                  hint="more than 1 and less than 100"
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
            @click="modifyProject(), (showModifyProject = false)"
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
    return { showModifyProject: false, loading: false };
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
  props: ["project"]
};
</script>
