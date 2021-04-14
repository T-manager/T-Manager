<template>
  <div style="display: flex; padding: 35px">
    <v-row justify="center">
      <v-col cols="6">
        <v-form v-model="valid">
          <v-card class="userLoginCard" ref="form">
            <v-card-title>Sign in with your account</v-card-title>
            <v-card-text>
              <v-text-field
                outlined
                ref="userName"
                v-model="userName"
                :rules="[rules.required]"
                label="Enter userName"
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
              <v-btn text @click="cancel"> Cancel </v-btn>
              <v-spacer></v-spacer>
              <v-slide-x-reverse-transition>
                <v-tooltip v-if="valid" left>
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
              <v-btn
                :disabled="!this.valid"
                color="primary"
                text
                @click="submit"
              >
                Submit
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      userName: null,
      userPassword: null,
      rules: {
        required: value => !!value || "This field is required."
      }
    };
  },
  computed: {
    form() {
      return {
        userName: this.userName,
        userPassword: this.userPassword
      };
    }
  },
  methods: {
    login: function() {
      this.$axios({
        method: "post",
        url: "http://localhost:6767/api/auth/login",
        data: {
          userName: this.userName,
          userPassword: this.userPassword
        }
      })
        .then(res => {
          if (res.data.data == 2000) alert("Login successfully");
          // 跳转首页 (未实现)
          if (res.data.data == 2001) alert("Wrong password");
          if (res.data.data == 2002) {
            alert("User not exist");
          } else {
            this.$store.commit("set_token", res.data.data);
            this.$store.commit("set_username", this.userName);
          }
          console.log(this.$store.getters.getToken); // 临时看一下token
          //this.$router.go(0);
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
    cancel() {
      console.log("cancel");
    }
  }
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
