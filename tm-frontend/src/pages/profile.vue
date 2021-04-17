<template>
  <div style="display:flex;justify-content:center;padding-top:20px">
    <UploadImg :userName="this.$store.getters.getUsername"></UploadImg>
    <div
      style=" margin-left:50px;display:flex;flex-direction:column;align-items:center;padding:25px;height:420px;width:900px;background-color:#ffffff;border-radius:20px"
    >
      <div
        style="margin-left:75px;margin-top:10px;font-size:26px;color:#101010;font-weight:bold;width:900px;text-align:start"
      >
        {{ !submit ? "Personal Profile" : "Edit Profile" }}
      </div>
      <!-- 编辑信息 -->
      <div v-if="submit" style="width:700px;">
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
      <!-- 查看信息 -->
      <div
        v-if="!submit"
        style="width:700px;display:flex;flex-direction:column;font-size:20px;margin-bottom:30px;"
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
          <div style="margin-left:30px;  line-height: 40px;">********</div>
          <div style="margin-left:100px;display:flex; ">
            <ModifyPassword :userEdit="userEdit"></ModifyPassword>
          </div>
        </div>
      </div>
      <v-btn
        v-if="!submit"
        style="width:600px;margin-top:20px"
        color="primary"
        outlined
        text
        @click="submit = true"
      >
        Edit
      </v-btn>
      <div style="display:flex;width:500px;margin-top:25px">
        <v-btn v-if="submit" text @click="submit = false"> Cancel </v-btn>
        <v-spacer></v-spacer>

        <v-btn
          v-if="submit"
          style=""
          color="primary"
          outlined
          text
          :loading="loading"
          :disabled="loading"
          @click="editProfile()"
        >
          Submit
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
  margin-top: 30px;
  margin-left: 30px;
  height: 40px;

  font-size: 20px;
  color: #101010;
}

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
