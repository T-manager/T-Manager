<template>
  <v-container style="display: flex; justify-content: center; padding: 35px">
    <v-form v-model="valid" v-on:submit.prevent>
      <v-card class="userLoginCard" ref="form" outlined>
        <div style="display: flex">
          <div style="font-size: 30px; margin-left: 10px; margin-bottom: 30px">
            Sign In
          </div>
          <v-spacer></v-spacer>
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
        </div>
        <div style="display: flex">
          <div style="width: 300px">
            <v-img
              :src="require('../assets/TmanagerLogo.svg')"
              height="175"
              contain
              style="margin-right: 20px"
            ></v-img>
            <v-img
              :src="require('../assets/TManager_text.svg')"
              height="100"
              width="200"
              style="margin-top: -15px; margin-left: 45px"
            ></v-img>
          </div>
          <div style="width: 300px">
            <v-text-field
              outlined
              ref="userName"
              v-model="userName"
              :rules="[rules.required]"
              label="Username"
              placeholder="Please enter your username"
              color="primary"
              style="margin-top: 8px"
            ></v-text-field>
            <v-text-field
              v-on:keyup.enter="loginHandler"
              outlined
              ref="userPassword"
              v-model="userPassword"
              :rules="[rules.required]"
              label="Password"
              placeholder="Please enter your password"
              color="primary"
              type="password"
            ></v-text-field>
            <div
              style="
                width: 100%;
                display: flex;
                justify-content: flex-end;
                margin-top: -10px;
              "
            >
              <forgetPwdDialog></forgetPwdDialog>
            </div>
            <div
              style="display: flex; justify-content: center; margin-top: 40px"
            >
              <userRegisterDialog></userRegisterDialog>
              <v-btn
                :disabled="!this.valid"
                color="primary"
                @click="submit"
                depressed
                style="margin-left: 10px; width: 100px; margin-left: 50px"
              >
                Sign In
              </v-btn>
            </div>
          </div>
        </div>
        <div
          style="
            text-decoration: underline;
            color: #6271c2;
            cursor: pointer;
            margin: 20px 0px 0px 10px;
            width: 100%;
            text-align: left;
          "
          @click="gotoHelp"
        >
          Help
        </div>
      </v-card>
    </v-form>
  </v-container>
</template>

<script>
import userRegisterDialog from "@/components/userRegisterDialog.vue";
import forgetPwdDialog from "@/components/forgetPwdDialog.vue";
export default {
  components: { userRegisterDialog, forgetPwdDialog },
  data() {
    return {
      valid: false,
      userName: "",
      userPassword: "",
      showBeginReset: false,
      showVerifyInfo: false,
      rules: {
        required: (value) => !!value || "This field is required.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
        length: (value) =>
          value.length <= 20 || "Must be less than 20 characters",
        validChar: (value) => {
          const pattern = /^[a-zA-Z0-9&@.$%\-_,():;` ]+$/;
          return pattern.test(value) || "Contains illegal characters";
        },
      },
    };
  },
  computed: {
    form() {
      return {
        userName: this.userName,
        userPassword: this.userPassword,
      };
    },
  },
  methods: {
    loginHandler() {
      if (
        this.$refs.userName.validate() &&
        this.$refs.userPassword.validate()
      ) {
        this.login();
      }
    },
    async login() {
      await this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/login",
        data: {
          userName: this.userName,
          userPassword: this.userPassword,
        },
      })
        .then((res) => {
          if (res.data.message == "Success") {
            this.$store.commit("set_username", this.userName);
            this.$store.commit("set_token", res.data.data);
            alert("Login sucessfully");
            this.getAvater();
          } else {
            this.$store.commit("response", res.data);
            this.$store.commit("del_username");
          }
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    },
    getAvater() {
      this.$axios({
        method: "get",
        url: this.$store.state.host + "user/get/" + this.userName,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken,
        },
      })
        .then((response) => {
          this.$store.commit("set_userphoto", response.data.data.userAvatar);
          this.$router.push("/project");
          this.$router.go(0);
        })
        .catch((error) => {
          this.$store.commit("response", error);
        });
    },
    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach((f) => {
        this.$refs[f].reset();
      });
    },
    submit() {
      this.login();
    },
    register() {
      this.showRegisterDialog = true;
    },
    gotoHelp() {
      window.open("https://ecb29d.baklib-free.com/01c8", "_blank");
    },
  },
};
</script>
<style scoped>
.userLoginCard {
  justify-content: center;
  align-items: center;
  padding: 30px 35px 20px 35px;
  border-radius: 10px;
  opacity: 0.9;
}
</style>
