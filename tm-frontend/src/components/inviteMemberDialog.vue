<template>
  <div>
    <v-btn color="primary" text>
      Invite members
    </v-btn>
    <!-- 点击邀请进入成员dialog -->
    <v-dialog v-model="showUserInviteDialog" persistent max-width="600px">
      <template v-slot:activator="{ on, attrs }"> </template>
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
    return { showUserInviteDialog: false };
  }
};
</script>
