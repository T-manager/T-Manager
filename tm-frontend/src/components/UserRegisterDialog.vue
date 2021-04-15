<template>
  <div>
    <v-btn @click="showRegisterDialog = true" text> Register </v-btn>
    <v-dialog v-model="showRegisterDialog" persistent max-width="500px">
      <v-row no-gutters>
        <v-col cols="12">
          <v-form v-model="valid">
            <v-card class="userRegistCard" ref="form">
              <v-card-title>Create an new account</v-card-title>
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
                <v-text-field
                  outlined
                  ref="userPassword"
                  v-model="userPassword"
                  :rules="[rules.required]"
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
              <v-card-actions>
                <v-btn text @click="cancel"> Cancel </v-btn>
                <v-slide-x-reverse-transition>
                  <v-tooltip v-if="this.valid" right>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        class="my-0"
                        v-bind="attrs"
                        @click="resetForm"
                        v-on="on"
                      >
                        <v-icon>mdi-refresh</v-icon>
                      </v-btn>
                    </template>
                    <span>Refresh form</span>
                  </v-tooltip>
                </v-slide-x-reverse-transition>
                <v-spacer></v-spacer>
                <v-btn :disabled="!this.valid" color="primary" @click="submit">
                  Submit
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
      showRegisterDialog: false,
      userName: null,
      userPassword: null,
      userEmail: null,
      rules: {
        required: (value) => !!value || "This field is required.",
        email: (value) => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        },
      },
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
};
</script>
<style scoped>
</style>