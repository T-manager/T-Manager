<template>
  <div>
    <v-btn icon @click="showModifyPwd = true" color="primary">
      <v-icon>mdi-key</v-icon></v-btn
    >
    <v-dialog v-model="showModifyPwd" persistent max-width="600px">
      <div
        style="height:400px;background-color:#FFFFFF;display:flex;flex-direction:column;align-items:center;"
      >
        <div
          @click="showModifyPwd = false"
          style="display:flex;justify-content:flex-end;margin-top:5px;height:30px;width:570px"
        >
          <v-icon style="font-size:26px;cursor:pointer;"> mdi-close</v-icon>
        </div>
        <div
          style="font-size:24px;color:#101010;margin-top:-10px;width:520px;text-align:start;"
        >
          Edit Password
        </div>
        <div
          style="display:flex;flex-direction:column;justify-content:center;align-items:center;width:100%;margin-top:20px"
        >
          <v-text-field
            style="margin-top:20px;width:375px;"
            outlined
            label="New password"
            v-model="newPassword"
            :rules="rules.required"
            type="password"
          ></v-text-field>
          <v-text-field
            outlined
            label="Confirmation"
            style="width:375px"
            v-model="confirmPassword"
            :rules="rules.required"
            type="password"
          ></v-text-field>
        </div>
        <div
          class="d-flex justify-center"
          style="font-color:#101010;font-size:16px;margin-top:39px"
        >
          <v-btn
            style="margin-left:194pxl;margin-right:194px;width:94px;height:39px;border: 1px solid rgba(187,187,187,100);border-radius:10px;"
            depressed
            outlined
            @click="showModifyPwd = false"
            >CANCLE</v-btn
          >
          <v-spacer></v-spacer>
          <v-btn
            style="width:94px;height:39px;border-radius:10px"
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
      showModifyPwd: false,
      newPassword: "",
      confirmPassword: "",
      loading: false,
      rules: {
        required: [v => v.length > 0 || "This item is required!"]
      }
    };
  },
  props: ["userEdit"],
  methods: {
    logout(){
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
    async submit() {
      this.loading = true;
      if (!this.checkPwdRules(this.confirmPassword)) {
        this.loading = false;
        return;
      }

      if (this.newPassword != this.confirmPassword) {
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
            this.logout()
          })
          .catch(error => {
            console.log(error);
            //   this.$store.commit("response", error);
            this.loading = false;
          });
      }
    }
  }
};
</script>

<style scoped>
@-moz-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-webkit-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@-o-keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
@keyframes loader {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
