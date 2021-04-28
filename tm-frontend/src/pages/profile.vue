<template>
  <div style="display:flex;justify-content:center;padding-top:20px">
    <uploadImg :userName="this.$store.getters.getUsername"></uploadImg>
    <v-card
      style="margin-left:30px; margin-top:60px; display:flex;flex-direction:column;align-items:center; padding: 30px 50px 50px 50px; 
      height:420px; width:700px; border-radius:20px; border:1px solid #ddd"
      color="rgba(255, 255, 255, 0.8)"
      flat
    >
      <div
        style="margin-top:10px;font-size:26px;color:#101010;font-weight:bold;width:100%; text-align:start"
      >
        {{ !submit ? "Personal Profile" : "Edit Profile" }}
      </div>
      <!-- edit user information -->
      <div v-if="submit" style="width:100%;">
        <v-text-field
          outlined
          ref="username"
          v-model="userEdit.userName"
          :rules="[rules.required]"
          label="Enter username"
          color="primary"
          style="margin-top: 40px"
        ></v-text-field>
        <v-text-field
          outlined
          v-model="userEdit.userEmail"
          ref="email"
          :rules="[rules.required, rules.email]"
          label="Enter E-mail"
          color="primary"
          style="margin-top:20px"
        ></v-text-field>
      </div>
      <!-- show user information -->
      <div
        v-if="!submit"
        style="width:100%; display:flex;flex-direction:column; font-size:16px; margin-top:30px; margin-bottom:30px;"
      >
        <div class="line">
          <div style="width:200px">User Name:</div>
          <div style="margin-left:30px">{{ userInfo.userName }}</div>
        </div>
        <div class="line">
          <div style="width:200px">Email:</div>
          <div style="margin-left:30px;">{{ userInfo.userEmail }}</div>
        </div>
        <div class="line">
          <div style="width:200px">Password:</div>
          <div style="margin-left:30px; margin-top:5px">********</div>
          <div style="margin-left:100px;display:flex; ">
            <modifyPassword :userEdit="userEdit"></modifyPassword>
          </div>
        </div>
        <v-btn
          class="line"
          v-if="!submit"
          depressed
          style="border:#cccccc solid 1px; color:#777777; width:100%; margin-top:20px"
          @click="submit = true"
        >
          Edit
        </v-btn>
      </div>
      <div
        style="display:flex; justify-content:center; margin-top:10px"
        v-if="submit"
      >
        <v-btn
          @click="submit = false"
          depressed
          style="border:#cccccc solid 1px; color:#777777; width:100px"
        >
          Cancel
        </v-btn>
        <v-btn
          depressed
          color="primary"
          style="color:#fff; width:100px; margin-left:50px"
          :loading="loading"
          :disabled="loading"
          @click="editProfile()"
        >
          Submit
        </v-btn>
      </div>
    </v-card>
  </div>
</template>

<script>
import modifyPassword from "@/components/modifyPassword";
import uploadImg from "@/components/uploadImg";

export default {
  data() {
    return {
      loading: false,
      userInfo: {},
      imgFile: "",
      submit: false,
      userEdit: {
        userName: "",
        userEmail: "",
        userAvatar: ""
      },
      valid: false,
      password: null,

      rules: {
        required: value => !!value || "This field is required.",
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        }
      }
    };
  },
  components: {
    modifyPassword,
    uploadImg
  },

  methods: {
    async editProfile() {
      this.loading = true;
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
          this.$store.commit("set_username", this.userEdit.userName);
          this.loading = false;
          this.submit = false;
          // this.showModifyTodolist = false;
          this.$router.go(0);
          alert("success!");
        })
        .catch(error => {
          // console.log(error);
          // this.$store.commit("response", error);
          this.loading = false;
        });
    },

    resetForm() {
      this.errorMessages = [];
      Object.keys(this.form).forEach(f => {
        this.$refs[f].reset();
      });
    }
  },
  created() {
    if (this.$store.getters.getToken == null) {
      alert("You are not signned in yet!");
      var path = "/login";
      this.$router.push({ path: path });
    }
    this.$axios({
      method: "get",
      url:
        this.$store.state.host + "user/get/" + this.$store.getters.getUsername,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(response => {
        this.userInfo = response.data.data;
        this.userEdit = response.data.data;
      })
      .catch(error => {
        // console.log(error);
      });
  }
};
</script>
<style scoped>
.line {
  display: flex;
  text-align: start;
  line-height: 60px;

  color: #101010;
}
</style>
