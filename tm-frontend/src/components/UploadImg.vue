<template>
  <div>
    <v-btn icon @click="showUploadImg = true" color="primary">
      <v-icon>mdi-image-plus</v-icon></v-btn
    >
    <v-dialog v-model="showUploadImg" persistent max-width="500px">
      <div
        style="height:500px;background-color:#FFFFFF;display:flex;flex-direction:column;align-items:center;"
      >
        <div
          @click="showUploadImg = false"
          style="display:flex;justify-content:flex-end;margin-top:5px;height:30px;width:490px"
        >
          <v-icon style="font-size:26px"> mdi-close</v-icon>
        </div>

        <div
          style="height:350px;width:350px;border:solid 1px;border-color:#a5a5a5"
        >
          <v-img :src="fileUrl" height="350" width="350"></v-img>
        </div>
        <div
          style="margin-top:20px;display:flex;flex-direction:row;align-items:center;justify-items:center;width:400px;"
        >
          <v-file-input
            outlined
            hide-input
            prepend-icon="mdi-image-plus"
            v-model="imgFile"
            @change="Preview_image()"
            color="#f7b249"
            accept="image/* "
          >
          </v-file-input>
          <div
            style="margin-top:13px;font-size:16px;margin-left:-200px;color:#101010"
          >
            Click upload
          </div>
          <v-spacer></v-spacer>
          <v-btn depressed @click="uploadImg"> upload</v-btn>
        </div>
      </div>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return { showUploadImg: false, imgFile: "", fileUrl: "" };
  },
  props: ["userName"],
  methods: {
    async uploadImg() {
      var originalFile = new FormData();
      originalFile.append("file", this.imgFile);
      await this.$axios({
        method: "post",
        url:
          this.$store.state.host +
          "resource/upload/" +
          this.$store.getters.getUsername,
        data: originalFile,
        headers: {
          Authorization: "Bearer " + this.$store.getters.getToken
        }
      })
        .then(res => {
          alert("Avatar uploaded successfully!");
          this.$router.go(0);
        })
        .catch(error => {
          console.log(error);
        });
    },
    Preview_image() {
      this.fileUrl = URL.createObjectURL(this.imgFile);
      console.log(this.fileUrl);
    }
  }
};
</script>

<style scoped></style>
