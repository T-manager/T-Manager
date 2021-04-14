<template>
  <v-card class="projectCard" max-width="344" v-if="show">
    <v-img
      src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
      height="200px"
    ></v-img>
    <div>
      <v-card-title> {{ project.projectName }} </v-card-title>
      <v-spacer></v-spacer>
      <div style="display: flex; align-items: flex-start">
        <!-- 点击铅笔修改项目 -->
        <v-dialog v-model="showModifyDialog" persistent max-width="600px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              icon
              @click="showModifyDialog = true"
              v-bind="attrs"
              v-on="on"
              color="primary"
            >
              <v-icon>mdi-pencil-outline</v-icon></v-btn
            >
            <v-btn
              icon
              color="primary"
              @click="deleteProject()"
              :loading="loading.delete"
              :disabled="loading.delete"
            >
              <v-icon>mdi-close</v-icon></v-btn
            >
          </template>
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
                @click="showModifyDialog = false"
                :loading="loading.modify"
                :disabled="loading.modify"
              >
                Close
              </v-btn>
              <!-- 保存dialog数据 -->
              <v-btn
                depressed
                color="primary"
                text
                @click="modifyProject(), (showModifyDialog = false)"
                :loading="loading.modify"
                :disabled="loading.modify"
              >
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </div>
    <!-- projectOnwer -->
    <div>
      <!-- project member list -->
      <div>
        <v-list rounded>
          <v-subheader>Member list</v-subheader>
          <v-list-item-group v-model="memberItem" color="primary">
            <div style="font-size: 24px"></div>
            <v-list-item v-for="(item, i) in items" :key="i">
              <v-list-item-icon>
                <v-icon v-text="item.icon"></v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title v-text="item.text"></v-list-item-title>
              </v-list-item-content>
              <v-list-item-icon>
                <v-icon>"mdi-delete"</v-icon>
              </v-list-item-icon>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </div>
    </div>
  </v-card>
</template>

<script>
export default {
  props: ["project"]
};
</script>
