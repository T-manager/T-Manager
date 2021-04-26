<template>
  <div>
    <v-btn
      @click="showRegisterDialog = true"
      depressed
      style="border: #cccccc solid 1px; color: #777777; width: 100px"
      >Sign Up
    </v-btn>
    <v-dialog v-model="showRegisterDialog" persistent max-width="450px">
      <v-form v-model="valid">
        <v-card class="userRegistCard" ref="form">
          <div style="display: flex">
            <div style="font-size: 30px; margin-left: 10px">Sign Up</div>
            <v-spacer></v-spacer>
            <v-tooltip v-if="this.valid" right>
              <template v-slot:activator="{ on, attrs }">
                <v-btn icon v-bind="attrs" @click="resetForm" v-on="on">
                  <v-icon>mdi-refresh</v-icon>
                </v-btn>
              </template>
              <span>Refresh form</span>
            </v-tooltip>
          </div>
          <v-card-text style="margin-top: 30px; padding: 10px">
            <v-text-field
              outlined
              ref="userName"
              v-model="userName"
              :rules="[rules.required, rules.length, rules.validChar]"
              :error-messages="userNameErr"
              label="Enter username"
              color="primary"
            ></v-text-field>
            <v-text-field
              outlined
              ref="userPassword"
              v-model="userPassword"
              :rules="[rules.required, rules.length, rules.validChar]"
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
          <div style="display: flex; justify-content: center; margin-top: 10px">
            <v-btn
              @click="cancel"
              depressed
              style="
                border: #cccccc solid 1px;
                color: #777777;
                margin-right: 50px;
                width: 100px;
              "
            >
              Cancel
            </v-btn>
            <v-btn
              :disabled="!this.valid"
              color="primary"
              @click="submit"
              style="color: #fff; width: 100px"
              depressed
            >
              Sign Up
            </v-btn>
          </div>
        </v-card>
      </v-form>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      showRegisterDialog: false,
      userName: "",
      userPassword: "",
      userEmail: "",
      rules: {
        required: (value) => value.length > 0 || "This field is required.",
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
      userNameErr: [],
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
    checkName: function (val) {
      this.$axios({
        method: "get",
        url: this.$store.state.host + "auth/check?username=" + val,
      })
        .then((res) => {
          this.userNameErr = res.data.data == 2000 ? ["Username already exists"]:[];
        })
        .catch((error) => {
          console.log(error);
        });
    },
    addNewUser: function () {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/register",
        data: {
          userName: this.userName,
          userPassword: this.userPassword,
          userEmail: this.userEmail,
        },
      })
        .then((res) => {
          if (res.data.data == 1000) {
            alert("Registered successfully");
            this.showRegisterDialog = false;
          }
          if (res.data.data == 1001)
            alert("MySQL server error, User is null or User already exists");
        })
        .catch((error) => {
          console.log(error);
        });
    },
    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach((f) => {
        this.$refs[f].reset();
      });
    },
    submit() {
      this.addNewUser();
      this.showRegisterDialog = false;
    },
    cancel() {
      this.showRegisterDialog = false;
    },
  },
  watch: {
    userName: function (val) {
      return this.checkName(val);
    },
  },
};
</script>
<style scoped>
.userRegistCard {
  padding: 30px 35px 50px 35px;
}
</style>
