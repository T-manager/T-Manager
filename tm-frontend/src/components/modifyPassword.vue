<template>
  <div>
    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon @click="showModifyPwd = true" color="primary" v-on="on">
          <v-icon>mdi-key-outline</v-icon></v-btn
        >
      </template>
      <span>Modify Password</span>
    </v-tooltip>
    <!-- modify password popup -->
    <v-dialog v-model="showModifyPwd" persistent max-width="600px">
      <v-card style="padding:30px 35px 50px 35px; border-radius:10px">
        <div
          @click="showModifyPwd = false"
          style="display:flex;justify-content:flex-end; width:100%"
        >
          <v-icon style="font-size:26px;cursor:pointer;"> mdi-close</v-icon>
        </div>
        <div
          style="font-size:30px;color:#101010;margin-top:-10px;width:520px;text-align:start;"
        >
          Edit Password
        </div>
        <v-card-text style="margin-top:30px; padding:10px">
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
        <div style="display:flex; justify-content:center; margin-top:10px">
          <!-- close popup -->
          <v-btn
            depressed
            style="border:#cccccc solid 1px; color:#777777; width:100px"
            @click="showModifyPwd = false"
            >CANCLE</v-btn
          >
          <!-- submit modification -->
          <v-btn
            depressed
            color="primary"
            style="color:#fff; width:100px; margin-left:50px"
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
      showModifyPwd: false,
      newPassword: "",
      confirmPassword: "",
      loading: false,
      // inline check rules
      rules: {
        required: [v => v.length > 0 || "This item is required!"],
        length: value =>
          value.length <= 20 || "Must be less than 20 characters",
        validChar: value => {
          const pattern = /^[a-zA-Z0-9&@.$%\-_,():;` ]+$/;
          return pattern.test(value) || "Contains illegal characters";
        }
      }
    };
  },
  props: ["userEdit"],
  methods: {
    /** logout method */
    logout() {
      // delete token, name, photo stored in local storage
      this.$store.commit("del_token");
      this.$store.commit("del_username");
      this.$store.commit("del_userphoto");
      alert("Log out successfully！");
      var path = "/login";
      this.$router.push({ path: path });
      this.$router.go(0);
    },
    checkPwdRules(v) {
      if (typeof v == "undefined") return false;
      return v.length > 0;
    },
    /** apply for modify password */
    async submit() {
      this.loading = true;
      if (!this.checkPwdRules(this.confirmPassword)) {
        // reject if not satisfy all rules
        this.loading = false;
        return;
      }
      if (this.newPassword != this.confirmPassword) {
        // reject if confirm is not correct
        this.loading = false;
        alert("The two passwords are inconsistent");
      } else {
        this.userEdit.userPassword = this.confirmPassword;
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
            this.logout();
          })
          .catch(error => {
            console.log(error);
            // this.$store.commit("response", error);
            this.loading = false;
          });
      }
    }
  }
};
</script>
