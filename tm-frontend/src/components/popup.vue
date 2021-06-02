<template>
  <!-- delete confirm popup -->
  <v-dialog v-model="showPopup" persistent max-width="400px">
    <v-card
      class="popup-background"
      style="padding:20px 20px 30px 20px; display:flex; align-items:center; flex-direction:column"
    >
      <div style="color:#D9DDF0; text-align:left; width:100%;">T-Manager</div>
      <div style="margin-bottom:30px; margin-top:10px; font-size:18px;">
        {{ message }}
      </div>
      <div style="display:flex; width:250px">
        <!-- give up the request -->
        <v-btn
          depressed
          style="border:#cccccc solid 1px; color:#777777; width:100px"
          @click="cancel"
          >Cancel</v-btn
        >
        <v-spacer></v-spacer>
        <!-- confirm delete request -->
        <v-btn
          @click="confirm"
          depressed
          color="primary"
          style="color:#fff; width:100px"
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
    /** using parent component's method to execute delete action */
    confirm() {
      this.$emit("confirmOperation");
    },
    /** using parent component's method to close the popup */
    cancel() {
      this.$emit("showPopupMethod");
    }
  },
  props: ["message", "showPopup"]
};
</script>

<style scoped>
.popup-background {
  background-image: url("../assets/TmanagerLogo_l5.svg");
  background-size: 240px;
  background-repeat: no-repeat;
  background-position: 175px -35px;
}
</style>
