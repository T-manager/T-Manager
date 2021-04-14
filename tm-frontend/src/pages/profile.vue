<template>
  <div style="display:flex;justify-content:center;padding-top:20px">
    <v-avatar size="150">
      <v-img src="https://picsum.photos/200"></v-img>
    </v-avatar>

    <div
      v-model="valid"
      style=" margin-left:50px;display:flex;flex-direction:column;padding:25px;height:400px;width:900px;background-color:#ffffff;border-radius:20px"
    >
      <div style="font-size:26px;color:#101010;width:900px;text-align:start">
        {{ !submit ? "Personal Profile" : "Edit Profile" }}
      </div>
      <div v-if="submit" style="margin-left:100px;display:flex; ">
        <UploadImg :userName="this.$store.getters.getUsername"></UploadImg>
        <ModifyPassword :userEdit="userEdit"></ModifyPassword>
      </div>
      <!-- 编辑信息 -->
      <div v-if="submit" style="width:700px;margin-left:75px">
        <v-text-field
          outlined
          ref="username"
          v-model="userEdit.userName"
          :rules="[rules.required]"
          label="Enter username"
          color="primary"
          style="margin-top: 8px"
        ></v-text-field>
        <v-text-field
          outlined
          v-model="userEdit.userEmail"
          ref="email"
          :rules="[rules.required, rules.email]"
          label="Enter E-mail"
          color="primary"
        ></v-text-field>
      </div>
      <!-- 查看信息 -->
      <div
        v-if="!submit"
        style="width:500px;display:flex;flex-direction:column;justify-self:center;font-size:20px;"
      >
        <div class="line">
          <div style="width:200px">User Name:</div>
          <div style="margin-left:30px">{{ userInfo.userName }}</div>
        </div>
        <div class="line">
          <div style="width:200px">Email:</div>
          <div style="margin-left:30px">{{ userInfo.userEmail }}</div>
        </div>
      </div>
      <div style="margin-top:0px;display:flex;width:700px;margin-left:75px">
        <v-btn v-if="submit" text @click="submit = false"> Cancel </v-btn>
        <v-spacer></v-spacer>

        <v-btn
          color="primary"
          text
          @click="submit == true ? editProfile() : (submit = true)"
        >
          {{ submit == true ? "Submit" : "Edit" }}
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import ModifyPassword from "@/components/ModifyPassword";
import UploadImg from "@/components/UploadImg";

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
    ModifyPassword,
    UploadImg
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
          console.log(res);
          this.$store.commit("set_username", this.userEdit.userName);
          this.loading = false;
          this.submit = false;
          // this.showModifyTodolist = false;
          this.$router.go(0);
          alert("success!");
        })
        .catch(error => {
          console.log(error);
          //   this.$store.commit("response", error);
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
        console.log(this.userInfo);
        console.log(this.$store.getters.getToken);
      })
      .catch(error => {
        console.log(error);
      });
  }
};
</script>
<style scoped>
.line {
  display: flex;
  text-align: start;
  margin-top: 50px;
  margin-left: 30px;
}
</style>
