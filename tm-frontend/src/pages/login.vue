<template>
  <div style="display: flex; padding: 35px">
    <v-row justify="center">
      <v-col cols="6">
        <v-form v-model="valid">
          <v-card class="userLoginCard" ref="form" outlined>
            <v-card-title>Sign in with your account</v-card-title>
            <v-card-text>
              <v-text-field
                outlined
                ref="userName"
                v-model="userName"
                :rules="[rules.required]"
                label="Enter username"
                color="primary"
                style="margin-top: 8px"
              ></v-text-field>
              <v-text-field
                outlined
                ref="userPassword"
                v-model="userPassword"
                :rules="[rules.required]"
                label="Enter password"
                color="primary"
                type="password"
              ></v-text-field>
            </v-card-text>
            <v-card-actions>
              <user-register-dialog></user-register-dialog>
              <forget-pwd-dialog></forget-pwd-dialog>
              <v-slide-x-reverse-transition>
                <v-tooltip v-if="valid" right>
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn
                      icon
                      class="my-0"
                      v-bind="attrs"
                      @click="resetForm"
                      v-on="on"
                    >
                      <v-icon>mdi-refresh</v-icon>
                    </v-btn>
                  </template>
                  <span>Refresh form</span>
                </v-tooltip>
              </v-slide-x-reverse-transition>
              <v-spacer></v-spacer>
              <v-btn :disabled="!this.valid" color="primary" @click="submit">
                SUBMIT
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import UserRegisterDialog from '@/components/UserRegisterDialog.vue';
import ForgetPwdDialog from '@/components/forgetPwdDialog.vue';
export default {
  components: { UserRegisterDialog, ForgetPwdDialog},
  data() {
    return {
      valid: false,
      userName: "",
      userPassword: "",
      showBeginReset: false,
      showVerifyInfo: false,
      rules: {
        required: value => !!value || "This field is required."
      },
    };
  },
  computed: {
    form() {
      return {
        userName: this.userName,
        userPassword: this.userPassword
      };
    },
  },
  methods: {
    login: function() {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/login",
        data: {
          userName: this.userName,
          userPassword: this.userPassword
        }
      })
        .then(res => {
          //if (res.data.data == 2000) 登录成功返回的直接就是cookie
          if (res.data.data == 2001) alert("Wrong password");
          else if (res.data.data == 2002) alert("User not exist");
          else {
            this.$store.commit("set_username", this.userName);
            this.$store.commit("set_token", res.data.data);
            alert("Login sucessfully");
            this.$router.push("/project")
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach(f => {
        this.$refs[f].reset();
      });
    },
    submit() {
      this.login();
    },
    register() {
      this.showRegisterDialog = true;
    },
  },
};
</script>
<style scoped>
.userLoginCard {
  min-width: 450px;
  margin: 15px;
  justify-content: center;
  align-items: center;
  padding: 10px 10px 0px 10px;
}
</style>