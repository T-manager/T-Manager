<template>
  <div style="display: flex; padding: 35px">
    <v-row justify="center">
      <v-col cols="6">
        <v-form v-model="valid">
          <v-card class="userRegistCard" ref="form">
            <v-card-title>Create an new account</v-card-title>
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
              <v-text-field
                outlined
                v-model="userEmail"
                ref="userEmail"
                :rules="[rules.required, rules.email]"
                label="Enter E-mail"
                color="primary"
              ></v-text-field>
            </v-card-text>
            <v-card-actions>
              <v-btn text @click="cancel"> Cancel </v-btn>
              <v-spacer></v-spacer>
              <v-slide-x-reverse-transition>
                <v-tooltip v-if="this.valid" left>
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
              <v-btn :disabled="!this.valid" color="primary" text @click="submit">  Submit </v-btn>
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
      userEmail: null,
      rules: {
        required: (value) => !!value || "This field is required.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
      }
    };
  },
  computed: {
    form() {
      return {
        userName: this.userName,
        userPassword: this.userPassword,
        userEmail: this.userEmail,
      };
    },
  },
  methods: {
    addNewUser: function () {
      this.$axios({
        method: "post",
        url: "http://localhost:6767/api/auth/register",
        data: {
          userName: this.userName,
          userPassword: this.userPassword,
          userEmail: this.userEmail,
        },
      })
        .then((res) => {
          if (res.data.data == 1000) alert("Registered successfully");
          // 跳转首页 (未实现)
          if (res.data.data == 1001) alert("User already exists");
          //this.$router.go(0);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    resetForm() {
      this.errorMessages = [];
      // bug: 当邮箱格式错误时无法reset
      Object.keys(this.form).forEach((f) => {
        this.$refs[f].reset();
      });
    },
    submit() {
      this.addNewUser();
    },
    cancel(){
      console.log("cancel")
    }
  },
};
</script>
<style scoped>
.userRegistCard {
  min-width: 450px;
  margin: 15px;
  justify-content: center;
  align-items: center;
  padding: 10px 10px 0px 10px;
}
</style>