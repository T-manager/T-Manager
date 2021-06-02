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
    <v-dialog v-model="showRegisterDialog" persistent max-width="500px">
      <v-form v-model="valid" v-on:submit.prevent>
        <v-card class="userRegistCard" ref="form">
          <div style="display: flex">
            <div style="font-size: 30px; margin-left: 10px">Sign Up</div>
            <v-btn
            color="primary"
            style="margin-left:5px; margin-top:5px"
            icon
            @click="gotoHelp"
          >
            <v-icon size="28">mdi-help-circle-outline</v-icon>
          </v-btn>
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
              @click="checkEmail"
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
      <v-form v-model="valid" v-on:submit.prevent>
        <v-card ref="form" class="userRegistCard">
          <div style="display: flex; font-size: 30px; margin-left: 10px">
            Input the verification code
          </div>
          <v-card-text style="margin-top: 30px; padding: 10px">
            <v-text-field
              v-on:keyup.enter="checkCodeHandler"
              outlined
              ref="userVerifyCode"
              v-model="userVerifyCode"
              :rules="[rules.required, rules.code]"
              label="Enter verification code"
              color="primary"
            ></v-text-field>
            <div
              style="
                width: 100%;
                display: flex;
                justify-content: flex-end;
                margin-top: -10px;
              "
            >
              <div
                style="
                  text-decoration: underline;
                  color: #6271c2;
                  cursor: pointer;
                "
                @click="resendCode"
              >
                {{ resendMessage }}
              </div>
            </div>
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
              @click="checkVerifyCode"
              style="color: #fff; width: 100px"
              depressed
            >
              Submit
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
      loading: false,
      resending: false,
      resendMessage: "Resend code",
      showRegisterDialog: false,
      showVerifyCode: false,
      userName: "",
      userPassword: "",
      userEmail: "",
      userVerifyCode: "",
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
        code: (value) => {
          const pattern = /^\d{6}$/;
          return pattern.test(value) || "6 digits only";
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
    /** User name verification*/
    checkName: function (val) {
      this.$axios({
        method: "get",
        url: this.$store.state.host + "auth/checkname?username=" + val,
      })
        .then((res) => {
          this.userNameErr =
            res.data.status == 321 ? ["Username already exists"] : [];
        })
        .catch((error) => {
          this.$store.commit("response", error);
        });
    },
    checkEmail: function() {
      this.$axios({
        method: "get",
        url: this.$store.state.host + "auth/checkemail?email=" + this.userEmail,
      })
        .then((res) => {
          if(res.data.status == 321)
            alert("Email already used"); 
          else
            this.getVerifyCode();
        })
        .catch((error) => {
          this.$store.commit("response", error);
        });
    },
    /**Create a new user*/
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
          alert("Registered successfully");
          this.showRegisterDialog = false;
        })
        .catch((error) => {
          this.$store.commit("response", error);
        });
    },
    /**Reset all input*/
    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach((f) => {
        this.$refs[f].reset();
      });
    },
    /**Wait before resend the verification code*/
    resendCode() {
      if (this.resending == false) {
        this.resending = true;
        this.getVerifyCode();
        let time = 60;
        let timer = setInterval(() => {
          if (time == 0) {
            clearInterval(timer);
            this.resending = false;
            this.resendMessage = "Resend code";
          } else {
            this.resendMessage = "Resend after " + time + " seconds";
            this.disabled = true;
            time--;
          }
        }, 1000);
      }
    },
    /**Get Verifycode and send it to email*/
    getVerifyCode: async function () {
      this.loading = true;
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/codesending",
        data: {
          userName: this.userName,
          userEmail: this.userEmail,
        },
      })
        .then((res) => {
            this.loading = false;
            this.showRegisterDialog = false;
            this.showVerifyCode = true;
          }
        )
        .catch((error) => {
          this.$store.commit("response", error);
          this.userEmail = null;
        });
    },
    checkCodeHandler() {
      if (this.$refs.userVerifyCode.validate()) {
        this.checkVerifyCode();
      }
    },
    checkVerifyCode: async function () {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/codeVerification",
        data: {
          userName: this.userName,
          userEmail: this.userEmail,
          verifyPassword: this.userVerifyCode,
        },
      })
        .then((res) => {
          if (res.data.data == "Verify successful") {
            this.submit();
          } else {
            alert(res.data.message);
          }
        })
        .catch((error) => {
          this.$store.commit("response", error);
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
    },
    gotoHelp() {
      window.open("https://ecb29d.baklib-free.com/01c8/a8c0", "_blank");
    },
  },
  watch: {
    userName: function (val) {
      return this.checkName(val);
    }
  },
};
</script>
<style scoped>
.userRegistCard {
  padding: 30px 35px 50px 35px;
}
</style>
