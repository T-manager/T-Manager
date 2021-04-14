<template>
  <div style=" padding: 35px">
    <v-row justify="center">
      <v-col cols="6">
        <v-form v-model="valid">
          <v-card flat class="userProfileCard" ref="form">
            <v-card-title>Edit Profile</v-card-title>
            <v-list-item>
              <v-list-item-avatar>
                <v-img src="https://picsum.photos/200"></v-img>
              </v-list-item-avatar>
            </v-list-item>
            <v-card-text v-if="submit">
              <v-text-field
                outlined
                ref="username"
                v-model="userEdit.userName"
                :rules="[rules.required]"
                label="Enter username"
                color="primary"
                style="margin-top: 8px"
              ></v-text-field>
              <v-text-field
                outlined
                v-model="userEdit.userEmail"
                ref="email"
                :rules="[rules.required, rules.email]"
                label="Enter E-mail"
                color="primary"
              ></v-text-field>
            </v-card-text>
            <v-card-text v-if="!submit">
              <v-text-field
                outlined
                disabled
                ref="username"
                v-model="userEdit.userName"
                :rules="[rules.required]"
                label="Enter username"
                color="primary"
                style="margin-top: 8px"
              ></v-text-field>
              <v-text-field
                outlined
                disabled
                v-model="userEdit.userEmail"
                ref="email"
                :rules="[rules.required, rules.email]"
                label="Enter E-mail"
                color="primary"
              ></v-text-field>
            </v-card-text>
            <v-card-actions>
              <v-btn v-if="submit" text @click="submit=false"> Cancel </v-btn>
              <v-spacer></v-spacer>

              <v-btn
                :disabled="!this.valid"
                color="primary"
                text
                @click="submit == true ? editProfile() : (submit = true)"
              >
                {{ submit == true ? "Submit" : "Edit" }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
    <div
      v-if="submit"
      style="margin-left:560px;margin-top:-340px;display:flex; background-color:red;width:300px"
    >
      <UploadImg :userName="this.$store.getters.getUsername"></UploadImg>
      <ModifyPassword :userEdit="userEdit"></ModifyPassword>
    </div>
  </div>
</template>

<script>
import ModifyPassword from "@/components/ModifyPassword";
import UploadImg from "@/components/UploadImg";

export default {
  data() {
    return {
      loading: false,
      userInfo: {},
      imgFile: "",
      submit: false,
      userEdit: {
        userName: "",
        userEmail: "",
        userAvatar: ""
      },
      valid: false,
      password: null,

      rules: {
        required: value => !!value || "This field is required.",
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        }
      }
    };
  },
  components: {
    ModifyPassword,
    UploadImg
  },

  methods: {
    async editProfile() {
      this.loading = true;
      await this.$axios({
        method: "put",
        url:
          this.$store.state.host +
          "user/edit/" +
          this.$store.getters.getUsername,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        },
        data: this.userEdit
      })
        .then(res => {
          console.log(res);
          this.$store.commit("set_username", this.userEdit.userName);
          this.loading = false;
          this.submit = false;
          // this.showModifyTodolist = false;
          this.$router.go(0);
          alert("success!");
        })
        .catch(error => {
          console.log(error);
          //   this.$store.commit("response", error);
          this.loading = false;
        });
    },

    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach(f => {
        this.$refs[f].reset();
      });
    },

    
  },
  created() {
    this.$axios({
      method: "get",
      url:
        this.$store.state.host + "user/get/" + this.$store.getters.getUsername,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(response => {
        this.userInfo = response.data.data;
        this.userEdit = response.data.data;
        console.log(this.userInfo);
        console.log(this.$store.getters.getToken);
      })
      .catch(error => {
        console.log(error);
      });
  }
};
</script>
<style scoped>
.userProfileCard {
  min-width: 450px;
  margin: 15px;
  justify-content: center;
  align-items: center;
  padding: 10px 10px 0px 10px;
}
</style>
