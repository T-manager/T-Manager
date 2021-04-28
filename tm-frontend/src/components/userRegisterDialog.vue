<template>
  <div>
    <!-- Button used to signup -->
    <v-btn
      @click="showRegisterDialog = true"
      depressed
      style="border: #cccccc solid 1px; color: #777777; width: 100px"
      >Sign Up
    </v-btn>
    <!-- Dialog for signup -->
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
              :loading="loading"
              color="primary"
              @click="getVerifyCode"
              style="color: #fff; width: 100px"
              depressed
            >
              NEXT
            </v-btn>
          </div>
        </v-card>
      </v-form>
    </v-dialog>
    <!-- Dialog for E-mail validation -->
    <v-dialog v-model="showVerifyCode" persistent max-width="500px">
      <v-row no-gutters>
        <v-col cols="12">
          <v-form v-model="valid">
            <v-card ref="form">
              <v-card-title
                >Input the code recieved from your E-mail</v-card-title
              >
              <v-card-text>
                <v-text-field
                  outlined
                  ref="userVerifyCode"
                  v-model="userVerifyCode"
                  :rules="[rules.required, rules.code]"
                  label="Enter verification code"
                  color="primary"
                  style="margin-top: 8px"
                ></v-text-field>
              </v-card-text>
              <v-card-actions>
                <v-btn text @click="cancel"> Cancel </v-btn>
                <v-spacer></v-spacer>
                <div
                  style="
                    text-decoration: underline;
                    color: #6271c2;
                    cursor: pointer;
                  "
                  @click="getVerifyCode"
                >
                  Resend code
                </div>
                <v-spacer></v-spacer>
                <v-btn
                  :disabled="!this.valid"
                  color="primary"
                  @click="checkVerifyCode"
                >
                  SIGN UP
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      loading: false,
      showRegisterDialog: false,
      showVerifyCode: false,
      userName: "",
      userPassword: "",
      userEmail: "",
      userVerifyCode: "",
      rules: {
        required: value => value.length > 0 || "This field is required.",
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
        length: value =>
          value.length <= 20 || "Must be less than 20 characters",
        validChar: value => {
          const pattern = /^[a-zA-Z0-9&@.$%\-_,():;` ]+$/;
          return pattern.test(value) || "Contains illegal characters";
        },
        code: value => {
          const pattern = /^\d{6}$/;
          return pattern.test(value) || "6 digits only";
        }
      },
      userNameErr: []
    };
  },
  computed: {
    form() {
      return {
        userName: this.userName,
        userPassword: this.userPassword,
        userEmail: this.userEmail
      };
    }
  },
  methods: {
    /** User name verification*/
    checkName: function(val) {
      this.$axios({
        method: "get",
        url: this.$store.state.host + "auth/check?username=" + val
      })
        .then(res => {
          this.userNameErr =
            res.data.data == 2000 ? ["Username already exists"] : [];
        })
        .catch(error => {
          // console.log(error);
        });
    },
    /**Create a new user*/
    addNewUser: function() {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/register",
        data: {
          userName: this.userName,
          userPassword: this.userPassword,
          userEmail: this.userEmail
        }
      })
        .then(res => {
          if (res.data.data == 1000) {
            alert("Registered successfully");
            this.showRegisterDialog = false;
          }
          if (res.data.data == 1001)
            alert("MySQL server error, User is null or User already exists");
        })
        .catch(error => {
          // console.log(error);
        });
    },
    /**Reset all input*/
    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach(f => {
        this.$refs[f].reset();
      });
    },
    /**Get Verifycode and send it to email*/
    getVerifyCode: async function() {
      this.loading = true;
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/codesending",
        data: {
          userName: this.userName,
          userEmail: this.userEmail
        }
      })
        .then(res => {
          if (res.data.data == 3000) {
            this.loading = false;
            this.showRegisterDialog = false;
            this.showVerifyCode = true;
          } else {
            alert("Email failed to send");
            this.userEmail = null;
          }
        })
        .catch(error => {
          // console.log(error);
        });
    },
    checkVerifyCode: async function() {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/codeVerification",
        data: {
          userName: this.userName,
          userEmail: this.userEmail,
          verifyPassword: this.userVerifyCode
        }
      })
        .then(res => {
          if (res.data.data == "Verify successful") {
            this.submit();
          } else {
            alert(res.data.message);
          }
        })
        .catch(error => {
          // console.log(error);
        });
    },
    submit() {
      this.addNewUser();
      this.cancel();
    },
    cancel() {
      (this.valid = false),
        (this.loading = false),
        (this.showRegisterDialog = false),
        (this.showVerifyCode = false),
        (this.userName = ""),
        (this.userPassword = ""),
        (this.userEmail = ""),
        (this.userVerifyCode = "");
    }
  },
  watch: {
    userName: function(val) {
      return this.checkName(val);
    }
  }
};
</script>
<style scoped>
.userRegistCard {
  padding: 30px 35px 50px 35px;
}
</style>
