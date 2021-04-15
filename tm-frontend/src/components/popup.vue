<template>
  <v-dialog v-model="showAlert" persistent max-width="600px">
    <v-card>
      <div><v-icon>mdi-close</v-icon></div>
      <div>{{ message }}</div>
      <div>
        <v-btn @click="showAlert = false">Cancel</v-btn>
        <v-btn @click="confirm">Confirm</v-btn>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: function() {
    return { showAlert: false, loading: false };
  },
  methods: {
    confirm() {
      this.$emit("confirmOperation");
    }
  },
  props: ["message"]
};
</script>
