<template>
	  <div style="display:flex; padding:35px">
      <v-row>
        <v-col cols="12">
          <v-card class="userRegistCard" ref="form">
            <v-card-text>
              <v-text-field outlined
                ref="username"
                v-model="username"
                :rules="[rules.required]"
                label="Enter username"
                color="primary"
                style="margin-top:8px"
              ></v-text-field>
              <v-text-field outlined
                ref="password"
                v-model="password"
                :rules="[rules.required]"
                label="Enter password"
                color="primary"
                type="password"
              ></v-text-field>
              <v-text-field outlined
                v-model="email"
                ref="email"
                :rules="[rules.required,rules.email]"
                label="Enter E-mail"
                color="primary"
              ></v-text-field>
            </v-card-text>
             <v-card-actions>
              <v-btn text>
                Cancel
              </v-btn>
              <v-spacer></v-spacer>
              <v-slide-x-reverse-transition>
                <v-tooltip
                  v-if="formHasErrors"
                  left
                >
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
              <v-btn
                color="primary"
                text
                @click="submit"
              >
                Submit
              </v-btn>
            </v-card-actions>
          </v-card>

        </v-col>
      </v-row>
    </div>
</template>

<script>
export default ({
data() {
  return {
			username: null,
			password: null,
      email:null,
      rules: {
        required: value => !!value || 'This field is required.',
        email: value => {
            const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            return pattern.test(value) || 'Invalid e-mail.'
        }
      },
      formHasErrors: false
		};
	},
  computed: {
      form () {
        return {
          username: this.username,
          password: this.password,
          email: this.email
        }
      }
  },
	methods: {
		addNewUser: function(){
			this.$axios({
        method: "post",
        url: "http://localhost:6767/api/user/regist",
        data: {
          username:this.username,
          password:this.password,
          email:this.email
        }
      })
        .then(res => {
          alert("成功提交，谢谢您的配合！");

          this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
		},
    resetForm () {
      this.errorMessages = []
      this.formHasErrors = false
      // bug: 当邮箱格式错误时无法reset
      Object.keys(this.form).forEach(f => {
        this.$refs[f].reset()
      })
    },
    submit () {
      this.formHasErrors = false
      Object.keys(this.form).forEach(f => {
        if (!this.form[f]) this.formHasErrors = true
        this.$refs[f].validate(true)
      })
      if(!this.formHasErrors)
        this.addNewUser();
    },
	}
})
</script>
<style scoped>
.userRegistCard{
  margin: 15px;
  justify-content: center;
  align-items: center;
  padding: 10px 10px 0px 10px;
}
</style>