<template>
  <v-dialog v-model="showPopup" persistent max-width="400px">
    <v-card
      class="background"
      style="padding:20px; display:flex; align-items:center; flex-direction:column"
    >
      <div style="margin-bottom:40px; margin-top:30px; font-size:18px">
        {{ message }}
      </div>
      <div style="display:flex; width:250px">
        <v-btn
          style="border:1px solid #aab2de; background-color:#fff"
          class="indigo--text"
          @click="cancel"
          depressed
          >Cancel</v-btn
        >
        <v-spacer></v-spacer>
        <v-btn color="primary" class="white--text" @click="confirm" depressed
          >Confirm</v-btn
        >
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: function() {
    return { loading: false };
  },
  methods: {
    confirm() {
      this.$emit("confirmOperation");
    },
    cancel() {
      this.$emit("showPopupMethod");
    }
  },
  props: ["message", "showPopup"]
};
</script>

<style scope>
.background {
  background-image: url("../assets/TManager.png");
  /* background-image: url("../assets/TManagerLogo.png"); */
  background-size: 100px;
  background-repeat: no-repeat;
  background-position: 0px -25px;
}
</style>
