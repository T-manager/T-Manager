<template>
  <div>
    <div
      style="text-decoration: underline; color: #6271c2; cursor: pointer"
      @click="showBeginReset = true"
    >
      Forget password?
    </div>
    <!-- Dialog for checking whether the username exists -->
    <v-dialog v-model="showBeginReset" persistent max-width="450px">
      <v-form v-model="valid">
        <v-card class="resetPwdCard" ref="form">
          <div style="display: flex; font-size: 30px; margin-left: 10px">
            Input your username
          </div>
          <v-card-text style="margin-top: 30px; padding: 10px">
            <v-text-field
              outlined
              ref="userName"
              v-model="userName"
              :rules="[rules.required]"
              label="Enter username"
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
              @click="checkExistUser"
              style="color: #fff; width: 100px"
              depressed
            >
              NEXT
            </v-btn>
          </div>
        </v-card>
      </v-form>
    </v-dialog>
    <!-- Dialog for checking whether email matches username -->
    <v-dialog v-model="showVerifyInfo" persistent max-width="500px">
      <v-form v-model="valid">
        <v-card class="resetPwdCard" ref="form">
          <div style="display: flex; font-size: 30px; margin-left: 10px">
            Input your Email address
          </div>
          <v-card-text style="margin-top: 30px; padding: 10px">
            <v-text-field
              outlined
              ref="userEmail"
              v-model="userEmail"
              :rules="[rules.required, rules.email]"
              label="Enter Email address"
              color="primary"
              style="margin-top: 8px"
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
              @click="checkUserInfo"
              style="color: #fff; width: 100px"
              depressed
            >
              NEXT
            </v-btn>
          </div>
        </v-card>
      </v-form>
    </v-dialog>
    <!-- Dialog for verification code -->
    <v-dialog v-model="showVerifyCode" persistent max-width="500px">
      <v-form v-model="valid">
        <v-card ref="form" class="resetPwdCard">
          <div style="display: flex; font-size: 30px; margin-left: 10px">
            Input the verification code
          </div>
          <v-card-text style="margin-top: 30px; padding: 10px">
            <v-text-field
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
                @click="getVerifyCode"
              >
                Resend code
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
              NEXT
            </v-btn>
          </div>
        </v-card>
      </v-form>
    </v-dialog>
    <!-- Dialog for modifying password -->
    <v-dialog v-model="showModifyPwd" persistent max-width="500px">
      <v-card style="padding: 30px 35px 50px 35px; border-radius: 10px">
        <div
          @click="showModifyPwd = false"
          style="display: flex; justify-content: flex-end; width: 100%"
        >
          <v-icon style="font-size: 26px; cursor: pointer"> mdi-close</v-icon>
        </div>
        <div
          style="
            font-size: 30px;
            color: #101010;
            margin-top: -10px;
            width: 520px;
            text-align: start;
          "
        >
          Edit Password
        </div>
        <v-card-text style="margin-top: 30px; padding: 10px">
          <v-text-field
            outlined
            label="New password"
            v-model="newPassword"
            :rules="[rules.required, rules.validChar, rules.length]"
            type="password"
          ></v-text-field>
          <v-text-field
            outlined
            label="Confirmation"
            v-model="confirmPassword"
            :rules="rules.required"
            type="password"
          ></v-text-field>
        </v-card-text>
        <div style="display: flex; justify-content: center; margin-top: 10px">
          <!-- close popup -->
          <v-btn
            depressed
            style="border: #cccccc solid 1px; color: #777777; width: 100px"
            @click="showModifyPwd = false"
            >CANCLE</v-btn
          >
          <!-- submit modification -->
          <v-btn
            depressed
            color="primary"
            style="color: #fff; width: 100px; margin-left: 50px"
            :loading="loading"
            :disabled="loading"
            @click="submit"
            >SUBMIT</v-btn
          >
        </div>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      showBeginReset: false,
      showVerifyInfo: false,
      showVerifyCode: false,
      showModifyPwd: false,
      userName: null,
      userEmail: null,
      userPassword: null,
      userVerifyCode: null,
      newPassword: "",
      confirmPassword: "",
      loading: false,
      rules: {
        required: (value) => !!value || "This field is required.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
        code: (value) => {
          const pattern = /^\d{6}$/;
          return pattern.test(value) || "6 digits only";
        },
      },
    };
  },
  methods: {
    cancel: function () {
      this.userName = "";
      this.userEmail = "";
      this.userVerifyCode = "";
      this.confirmPassword = "";
      this.newPassword = "";
      this.showBeginReset = false;
      this.showVerifyInfo = false;
      this.showVerifyCode = false;
      this.showModifyPwd = false;
    },
    next1: function () {
      this.showBeginReset = false;
      this.showVerifyInfo = true;
    },
    next2: function () {
      this.showVerifyInfo = false;
      this.showVerifyCode = true;
    },
    next3: function () {
      this.showVerifyCode = false;
      this.showModifyPwd = true;
    },
    checkExistUser: async function () {
      this.$axios({
        method: "get",
        url: this.$store.state.host + "auth/check?username=" + this.userName,
      })
        .then((res) => {
          if (res.data.data == 2000) {
            this.next1();
          } else {
            alert("User not exist");
            this.userName = null;
          }
        })
        .catch((error) => {
          // console.log(error);
        });
    },
    checkUserInfo: async function () {
      (this.loading = true),
        this.$axios({
          method: "post",
          url: this.$store.state.host + "auth/check",
          data: {
            userName: this.userName,
            userEmail: this.userEmail,
          },
        })
          .then((res) => {
            if (res.data.data == 2000) {
              this.getVerifyCode();
            } else {
              alert("Email not match");
              this.userEmail = null;
            }
          })
          .catch((error) => {
            // console.log(error);
          });
    },
    getVerifyCode: async function () {
      this.$axios({
        method: "post",
        url: this.$store.state.host + "auth/codesending",
        data: {
          userName: this.userName,
          userEmail: this.userEmail,
        },
      })
        .then((res) => {
          if (res.data.data == 3000) {
            this.loading = false;
            this.next2();
          } else {
            alert("Email failed to send");
            this.userEmail = null;
          }
        })
        .catch((error) => {
          // console.log(error);
        });
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
            this.next3();
          } else {
            alert(res.data.message);
          }
        })
        .catch((error) => {
          // console.log(error);
        });
    },
    checkPwdRules(v) {
      if (typeof v == "undefined") return false;
      return v.length > 0;
    },
    async submit() {
      this.loading = true;
      if (!this.checkPwdRules(this.confirmPassword)) {
        this.loading = false;
        return;
      }

      if (this.newPassword != this.confirmPassword) {
        alert("The two passwords are inconsistent");
      } else {
        this.userPassword = this.confirmPassword;

        await this.$axios({
          method: "put",
          url:
            this.$store.state.host +
            "auth/edit/" + //修改密码未完善，此处无需token认证即可修改密码
            this.userName,
          data: {
            userName: this.userName,
            userPassword: this.userPassword,
          },
        })
          .then((res) => {
            alert("Reset password successfully!");
            this.$store.commit("del_username");
            this.$store.commit("del_token");
            this.$router.go(0);
          })
          .catch((error) => {
            // console.log(error);
            //   this.$store.commit("response", error);
            this.loading = false;
          });
      }
    },
  },
};
</script>

<style scoped>
.resetPwdCard {
  padding: 30px 35px 50px 35px;
}
</style>
