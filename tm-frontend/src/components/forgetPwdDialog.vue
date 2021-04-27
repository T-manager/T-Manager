<template>
  <div>
    <div
      style="text-decoration: underline; color: #6271c2; cursor: pointer"
      @click="showBeginReset = true"
    >
      Forget password?
    </div>
    <v-dialog v-model="showBeginReset" persistent max-width="500px">
      <v-row no-gutters>
        <v-col cols="12">
          <v-form v-model="valid">
            <v-card ref="form">
              <v-card-title>Input your username</v-card-title>
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
              </v-card-text>
              <v-card-actions>
                <v-btn text @click="cancel"> Cancel </v-btn>
                <v-spacer></v-spacer>
                <v-btn
                  :disabled="!this.valid"
                  color="primary"
                  @click="checkExistUser"
                >
                  NEXT
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-dialog>
    <v-dialog v-model="showVerifyInfo" persistent max-width="500px">
      <v-row no-gutters>
        <v-col cols="12">
          <v-form v-model="valid">
            <v-card ref="form">
              <v-card-title>Input your Email address</v-card-title>
              <v-card-text>
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
              <v-card-actions>
                <v-btn text @click="cancel"> Cancel </v-btn>
                <v-spacer></v-spacer>
                <v-btn
                  :disabled="!this.valid"
                  color="primary"
                  @click="checkUserInfo"
                  :loading="loading"
                >
                  NEXT
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-dialog>
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
                  NEXT
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-dialog>
    <v-dialog v-model="showModifyPwd" persistent max-width="600px">
      <div
        style="
          height: 400px;
          background-color: #ffffff;
          display: flex;
          flex-direction: column;
          align-items: center;
        "
      >
        <div
          @click="showModifyPwd = false"
          style="
            display: flex;
            justify-content: flex-end;
            margin-top: 5px;
            height: 30px;
            width: 570px;
          "
        >
          <v-icon style="font-size: 26px; cursor: pointer"> mdi-close</v-icon>
        </div>
        <div
          style="
            font-size: 24px;
            color: #101010;
            margin-top: -10px;
            width: 520px;
            text-align: start;
          "
        >
          Edit Password
        </div>
        <div
          style="
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            width: 100%;
            margin-top: 20px;
          "
        >
          <v-text-field
            style="margin-top: 20px; width: 375px"
            outlined
            label="New password"
            v-model="newPassword"
            :rules="[rules.required]"
            type="password"
          ></v-text-field>
          <v-text-field
            outlined
            label="Confirmation"
            style="width: 375px"
            v-model="confirmPassword"
            :rules="[rules.required]"
            type="password"
          ></v-text-field>
        </div>
        <div
          class="d-flex justify-center"
          style="font-color: #101010; font-size: 16px; margin-top: 39px"
        >
          <v-btn
            style="
              margin-left: 194pxl;
              margin-right: 194px;
              width: 94px;
              height: 39px;
              border: 1px solid rgba(187, 187, 187, 100);
              border-radius: 10px;
            "
            depressed
            outlined
            @click="showModifyPwd = false"
            >CANCLE</v-btn
          >
          <v-spacer></v-spacer>
          <v-btn
            style="width: 94px; height: 39px; border-radius: 10px"
            depressed
            color="primary"
            :loading="loading"
            :disabled="loading"
            @click="submit"
            >SUBMIT</v-btn
          >
        </div>
      </div>
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
          console.log(error);
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
            console.log(error);
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
          console.log(error);
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
          console.log(error);
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
            "auth/edit/" + // ！！修改密码未完善，此处无需token认证即可修改密码
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
            console.log(error);
            //   this.$store.commit("response", error);
            this.loading = false;
          });
      }
    },
  },
};
</script>

<style scoped></style>
