<template>
  <div>
    <v-dialog
      v-model="$store.state.show.showAddTodo"
      persistent
      max-width="800px"
    >
      <v-card>
        <v-card-title>
          <span class="headline">采茶时光</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row class="d-flex justify-center">
              <v-col cols="12" xs="12" sm="6" md="6">
                <div>
                  <v-text-field
                    label="活动名称"
                    color="teal"
                    :rules="rules.nameRules"
                    counter="20"
                    hint="为你的采茶起一个有吸引力的名字吧！"
                    prepend-icon="mdi-leaf"
                    v-model="teaforenceForm.name"
                    :full-width="true"
                  ></v-text-field>
                </div>
                <!-- 上传采茶图片 -->
                <div>
                  <v-file-input
                    class="mt-2"
                    hint="为你的采茶添加图片"
                    color="teal"
                    v-model="imgFile"
                    @change="Preview_image"
                    accept="image/* "
                  ></v-file-input>
                </div>
              </v-col>
              <v-col cols="11" xs="12" sm="6" md="6">
                <v-card outlined @click.stop="showZoom = true">
                  <v-img
                    :src="fileUrl"
                    min-height="150"
                    max-height="200"
                  ></v-img>
                </v-card>
              </v-col>
              <v-col>
                <v-dialog v-model="showZoom" class="d-flex justify-center">
                  <v-img :src="fileUrl" max-height="800"> </v-img>
                </v-dialog>
              </v-col>
              <v-col cols="12" sm="12">
                <v-textarea
                  label="活动介绍"
                  required
                  color="teal"
                  :rules="rules.introRules"
                  counter="50"
                  auto-grow
                  rows="1"
                  hint="简单介绍一下活动的内容，方便大家了解哦"
                  prepend-icon="mdi-menu"
                  v-model="teaforenceForm.intro"
                ></v-textarea>
              </v-col>

              <v-col cols="12" sm="4">
                <v-menu
                  ref="datePicker"
                  v-model="datePicker"
                  :close-on-content-click="false"
                  :return-value.sync="teaforence.date"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="teaforence.date"
                      label="活动日期"
                      color="teal"
                      :rules="rules.notNull"
                      prepend-icon="mdi-calendar-outline"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker
                    v-model="teaforence.date"
                    no-title
                    scrollable
                    locale="zh-cn"
                    :first-day-of-week="1"
                    color="teal"
                  >
                    <v-spacer></v-spacer>
                    <v-btn color="teal lighten-4" @click="datePicker = false"
                      >取消</v-btn
                    >
                    <v-btn
                      color="teal"
                      dark
                      @click="$refs.datePicker.save(teaforence.date)"
                      >确认</v-btn
                    >
                  </v-date-picker>
                </v-menu>
              </v-col>

              <v-col cols="12" sm="4">
                <v-menu
                  ref="timePicker"
                  v-model="timePicker"
                  :close-on-content-click="false"
                  :nudge-right="40"
                  :return-value.sync="teaforence.time"
                  transition="scale-transition"
                  offset-y
                  max-width="290px"
                  min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="teaforence.time"
                      label="开始时间"
                      color="teal"
                      :rules="rules.notNull"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker
                    v-if="timePicker"
                    v-model="teaforence.time"
                    full-width
                    format="24hr"
                    color="teal"
                    @click:minute="$refs.timePicker.save(teaforence.time)"
                  ></v-time-picker>
                </v-menu>
              </v-col>
              <v-col cols="12" sm="4">
                <v-text-field
                  label="时长"
                  required
                  color="teal"
                  :rules="rules.durationRules"
                  hint="请填写数字，单位分钟"
                  prepend-icon="mdi-av-timer"
                  v-model="teaforenceForm.duration"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="4">
                <v-text-field
                  label="最大报名人数"
                  required
                  color="teal"
                  :rules="rules.limitNumRules"
                  hint="最大人数"
                  prepend-icon="mdi-map-outline"
                  v-model="teaforenceForm.limitNum"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="8">
                <v-text-field
                  label="地点"
                  required
                  color="teal"
                  :rules="rules.localRules"
                  counter="100"
                  hint="详细的地理位置信息, 让参加此次采茶的小伙伴能找到你"
                  prepend-icon="mdi-map-outline"
                  v-model="teaforenceForm.local"
                ></v-text-field>
              </v-col>

              <v-col
                cols="12"
                :sm="teaforenceForm.type == 'SHARE' ? '6' : '12'"
              >
                <v-select
                  prepend-icon="mdi-check-box-multiple-outline"
                  :rules="rules.notNull"
                  :items="['SHARE', 'DISCUSS']"
                  label="活动形式"
                  color="teal"
                  v-model="teaforenceForm.type"
                  persistent-hint
                  hint="分享可以为你带来可观的报酬, 讨论更有利于交流"
                ></v-select>
              </v-col>
              <v-col v-if="teaforenceForm.type == 'SHARE'" cols="12" sm="4">
                <v-text-field
                  prepend-icon="mdi-cup-outline"
                  :rules="rules.minCupRules"
                  label="品茶金额"
                  color="teal"
                  v-model="teaforenceForm.minCup"
                  persistent-hint
                  hint="课时费/人"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                sm="2"
                v-if="teaforenceForm.type == 'SHARE'"
                class="pt-6 center"
              >
                <v-btn @click="addLec" icons-and-text color="teal" dark>
                  <v-icon>mdi-account-plus-outline</v-icon>添加讲师
                </v-btn>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                class="center"
                v-for="i in teaforence.lecturers.length"
                :key="i"
              >
                <v-card v-if="teaforenceForm.type == 'SHARE'" class="rcC">
                  <v-text-field
                    label="讲师姓名"
                    required
                    color="teal"
                    class="ml-2 mr-1"
                    :rules="[
                      (teaforence.lecturers[i - 1].length <= 10 &&
                        teaforence.lecturers[i - 1].length > 0) ||
                        '1-10字'
                    ]"
                    prepend-icon="mdi-format-color-text"
                    v-model="teaforence.lecturers[i - 1]"
                  ></v-text-field>
                  <v-btn @click="delLec(i)" icon class="mr-2">
                    <v-icon>mdi-account-remove-outline</v-icon>
                  </v-btn>
                </v-card>
              </v-col>

              <v-col :cols="12" :sm="teaforence.tags_1 != null ? '7' : '12'">
                <v-select
                  :items="tags.level_1"
                  label="分类"
                  color="teal"
                  :rules="rules.notNull"
                  persistent-hint
                  v-model="teaforence.tags_1"
                  prepend-icon="mdi-palette-swatch-outline"
                  hint="为此次采茶添加标签分类"
                ></v-select>
              </v-col>
              <v-col v-if="teaforence.tags_1 != null" cols="12" sm="5">
                <v-select
                  :items="tags.level_2[teaforence.tags_1]"
                  v-model="teaforence.tags_2"
                  :rules="rules.notNull"
                  label="具体"
                  color="teal"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <!-- <v-textarea
                  outlined
                  label="活动详细介绍*"
                  :rules="rules.detailRules"
                  v-model="teaforenceForm.detail"
                  hint="此次采茶的详细介绍"
                  color="teal"
                  type="text"
                  counter="1000"
                  prepend-icon="mdi-script-text-outline"
                ></v-textarea> -->
                <vue-editor
                  v-model="detail"
                  v-on:ready="quill => (editorQuill = quill)"
                  placeholder="此次采茶的详细介绍15-1000字"
                />
                <span class="counter ma-3">{{ quillLength - 1 }}/1000</span>
              </v-col>
            </v-row>
          </v-container>
          <small>请自觉遵守社区规范, 违者可能进小黑屋哦</small>
        </v-card-text>
        <v-card-actions>
          <v-btn color="warning" dark @click="help = true">
            <v-icon class="pr-2">mdi-help-circle-outline</v-icon>帮助
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn color="teal lighten-4" light @click="commit = false">
            <v-icon class="pr-2">mdi-cancel</v-icon>取消
          </v-btn>
          <v-btn
            color="teal"
            class="white--text"
            @click="createTea()"
            :loading="loading"
            :disabled="loading"
          >
            <v-icon class="pr-2">mdi-cloud-upload-outline</v-icon>提交
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <!-- 帮助 -->
    <div class="text-center">
      <v-dialog v-model="help" width="500">
        <v-card>
          <v-card-title class="headline grey lighten-2">
            <i>开启采茶时光的小帖士：</i>
          </v-card-title>

          <v-card-text>
            在此页面，您可以新建采茶活动，包括线下讲座活动及线下讨论活动。
            您需要填写关于活动的具体信息，包括开展活动的具体时间地点、报名人数上限、讲师姓名以及课时费用，便于用户了解活动内容。同时您可以添加有关活动内容的图片，如海报等。</v-card-text
          >
          <v-card-text
            >我们的采茶活动有两种形式，即分享式和讨论式。</v-card-text
          >
          <v-card-text>
            <b> 1 分享式 (Share)：</b>

            作为讲座的主持人或发起人，您可以在“采茶时光”的页面上对讲座内容进行简单描述，吸引感兴趣的同学前来参加您认真筹备的讲座。
            讲座内部设有的线上讨论区：专门针对每一个采茶活动，所有同学都可以提出关于该讲座的相关问题，如授课内容的重点即难易程度。同时，同学们可以针对讲座内容中感兴趣的想法进行头脑风暴，发表个人见解。学霸笔记也统统安排上～
          </v-card-text>

          <v-card-text>
            <b> 2 讨论式 (Discuss)：</b>
            您可以根据感兴趣的话题发起线下交流。讨论式没有固定的主讲人，所有参与讨论活动的参与者可以围在一起对共同感兴趣的话题进行探讨。同学们可以在图书馆研讨室里畅谈英美文学，也可以在电气楼进行一场建模比赛的深刻交流。
          </v-card-text>
          <v-card-text>
            此外，建议您填写此次采茶活动的分类标签，便于用户寻找我们将活动分为三类，
            即课内研讨、课外交流和抱团组队。
          </v-card-text>
          <v-card-text>
            <b>1 课内研讨：</b>
            根据学校大类安排就课内研讨进行学科分类。包括理学院、人文社科学院、
            设计学院、西浦国际商学院、影视艺术学院、语言学院、智能工程学院及相关课内知识。
          </v-card-text>
          <v-card-text>
            <b>2 课外交流：</b>
            包括经验分享、运动健身、科技产品、手工美术、软件学习、语言学习、音乐影视、文学历史、哲学心理等。
          </v-card-text>
          <v-card-text>
            <b>3 抱团组队：</b>
            为西浦学生提供线上的多人公开交流空间，就课内组队，比赛/项目组队和约运动等热点问题进行信息互通与高效交流，并在认真严肃的学习之外，我们同样给大家留下了娱乐和兴趣爱好的空间，为同学们寻找可靠队友提供平台和机会同时形成以兴趣为导向的交友团体，包括组队桌游、密室、旅行、看电影、拍照等活动。
          </v-card-text>
          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="help = false">我明白了</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
    <v-tooltip left v-if="$store.getters.getToken != null">
      <template v-slot:activator="{ on }">
        <v-btn
          large
          v-on="on"
          color="pink"
          fab
          dark
          fixed
          right
          bottom
          @click="commit = true"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
      <span>发布采茶!</span>
    </v-tooltip>
  </div>
</template>

<script>
import { VueEditor } from "vue2-editor";
export default {
  components: { VueEditor },
  watch: {
    detail: function() {
      if (!!this.max && this.editorQuill.getLength() >= this.max) {
        this.editorQuill.deleteText(this.max, this.editorQuill.getLength());
      }
      this.quillLength = this.editorQuill.getLength();
    }
  },
  props: {
    max: {
      type: Number,
      default: 1000
    }
  },
  data() {
    return {
      editorQuill: "",
      quillLength: 1,
      detail: "",
      loading: false,
      tags: {
        level_1: ["课内研讨", "课外交流", "组队约局搞事情"],
        level_2: {
          课内研讨: [
            // 按照西浦学院进行划分
            "理学院",
            "人文社科学院",
            "设计学院",
            "西浦国际商学院",
            "影视艺术学院",
            "语言学院",
            "智能工程学院",
            "课内研讨-其他"
          ],
          课外交流: [
            "经验分享类",
            "运动健身类",
            "科技产品类",
            "手工美术类",
            "软件学习类",
            "语言学习类",
            "音乐影视类",
            "文学历史类",
            "哲学心理类",
            "课外交流-其他"
          ],
          组队约局搞事情: [
            "课内组队",
            "比赛/项目组队",
            "约自习",
            "约运动",
            "约玩", // 桌游、密室、旅行、看电影、拍照
            "组队约局搞事情-其他"
          ]
        }
      },
      commit: false,
      teaforenceForm: {
        times: 1,
        detail: ""
      },
      teaforence: {
        lecturers: [""]
      },
      help: false,
      dateFormat: new Date().toISOString().substr(0, 10),
      datePicker: false,
      timePicker: false,
      imgFile: "",
      fileUrl: "",
      hasImgUpload: 0,
      showZoom: 0,
      rules: {
        nameRules: [
          v =>
            (typeof v != "undefined" && v.length <= 20 && v.length >= 3) ||
            "请输入3-20字采茶标题"
        ],
        introRules: [
          v =>
            (typeof v != "undefined" && v.length <= 50 && v.length >= 5) ||
            "请输入5-50字活动介绍"
        ],
        detailRules: [
          v =>
            (typeof v != "undefined" && v.length >= 15 && v.length <= 1000) ||
            "字数请大于15少于1000"
        ],
        minCupRules: [
          v => (typeof v != "undefined" && /^[0-9]+$/.test(v)) || "此项必填"
        ],
        durationRules: [
          v => (typeof v != "undefined" && /^[0-9]+$/.test(v)) || "此项必填"
        ],
        localRules: [
          v =>
            (typeof v != "undefined" && v.length <= 100 && v.length >= 0) ||
            "此项必填"
        ],
        limitNumRules: [
          v => (typeof v != "undefined" && /^[0-9]+$/.test(v)) || "此项必填"
        ],
        notNull: [v => typeof v != "undefined" || "此项必填"]
      }
    };
  },
  methods: {
    addLec() {
      this.teaforence.lecturers.push("");
    },
    delLec(index) {
      if (this.teaforence.lecturers.length == 1) {
        alert("至少有一名讲师");
        return;
      }
      this.teaforence.lecturers.splice(index - 1, 1);
    },
    checkNameRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 20 && v.length >= 3;
    },
    checkIntroRules(v) {
      if (typeof v == "undefined") return false;
      return v.length <= 50 && v.length >= 5;
    },
    checkDetailRules(v) {
      if (typeof v == "undefined") return false;
      return v.length >= 15 && v.length <= 1000;
    },
    checkMinCupRules(v) {
      if (this.teaforenceForm.type == "DISCUSS") {
        this.teaforenceForm.minCup = 0;
        return true;
      }
      if (typeof v == "undefined") return false;
      let numReg = /^[0-9]+$/;
      return numReg.test(v);
    },
    checkTypeRules(v) {
      return typeof v != "undefined";
    },
    checkDateTimeRules(v) {
      return typeof v != "undefined";
    },
    checkDurationRules(v) {
      if (typeof v == "undefined") return false;
      let numReg = /^[0-9]+$/;
      return numReg.test(v);
    },
    checkLocalRules(v) {
      return typeof v != "undefined";
    },
    checkLimitNumRules(v) {
      if (typeof v == "undefined") return false;
      let numReg = /^[0-9]+$/;
      return numReg.test(v);
    },
    checkTagRules(v) {
      return typeof v != "undefined";
    },
    checkLecturersRules(v) {
      if (this.teaforenceForm.type == "DISCUSS") return true;
      for (var i in v) {
        if (v[i] == "") return false;
      }
      return true;
    },
    checkImgSizeRules(v) {
      if (v.size > 1024 * 1024 * 4) {
        return false;
      }
      return true;
    },
    checkRules() {
      if (!this.checkNameRules(this.teaforenceForm.name)) {
        alert("请填写name"); //改成聚焦到选框
        return false;
      }
      if (!this.checkIntroRules(this.teaforenceForm.intro)) {
        alert("请检查intro"); //改成聚焦到选框
        return false;
      }
      if (!this.checkDetailRules(this.teaforenceForm.detail)) {
        alert("请检查detail"); //改成聚焦到选框
        return false;
      }
      if (!this.checkTypeRules(this.teaforenceForm.type)) {
        alert("请检查type"); //改成聚焦到选框
        return false;
      }
      if (!this.checkMinCupRules(this.teaforenceForm.minCup)) {
        alert("请检查minCup"); //改成聚焦到选框
        return false;
      }
      if (!this.checkDateTimeRules(this.teaforence.date)) {
        alert("请检查开始日期的填写"); //改成聚焦到选框
        return false;
      }
      if (!this.checkDateTimeRules(this.teaforence.time)) {
        alert("请检查开始时间的填写"); //改成聚焦到选框
        return false;
      }
      if (!this.checkDurationRules(this.teaforenceForm.duration)) {
        alert("请检查duration"); //改成聚焦到选框
        return false;
      }
      if (!this.checkLocalRules(this.teaforenceForm.local)) {
        alert("请检查local"); //改成聚焦到选框
        return false;
      }
      if (!this.checkLimitNumRules(this.teaforenceForm.limitNum)) {
        alert("请检查limitNum"); //改成聚焦到选框
        return false;
      }
      if (!this.checkTagRules(this.teaforence.tags_1)) {
        alert("请检查tags"); //改成聚焦到选框
        return false;
      }
      if (!this.checkTagRules(this.teaforence.tags_2)) {
        alert("请检查tags"); //改成聚焦到选框
        return false;
      }
      if (!this.checkLecturersRules(this.teaforence.lecturers)) {
        alert("请检查lecturers"); //改成聚焦到选框
        return false;
      }
      if (!this.checkImgSizeRules(this.imgFile)) {
        alert("上传的图片不能大于4M");
        return false;
      }
      return true;
    },
    async createTea() {
      this.teaforenceForm.detail = this.detail;
      this.loading = true;
      this.teaforenceForm.startTime =
        this.teaforence.date + " " + this.teaforence.time + ":00";
      if (!this.checkRules()) {
        this.loading = false;
        return;
      }
      if (this.imgFile != "") {
        console.log(this.imgFile);
        await this.uploadImg();
      }
      await this.axios({
        method: "post",
        //headers: {
        //Authorization: "Bearer " + localStorage.getItem("token")
        //},
        url: this.$store.state.host + "teaforences/",
        data: this.teaforenceForm
      })
        .then(res => {
          //console.log("创建采茶");
          //console.log(res);
          this.axios({
            // 添加tag
            method: "post",
            url:
              this.$store.state.host +
              "teaforences/tags?tagname=" +
              this.teaforence.tags_1 +
              "&teaId=" +
              res.data.data.id
          })
            .then(res => {})
            .catch(error => {
              this.$store.commit("response", error);
            });
          this.axios({
            // 添加tag
            method: "post",
            url:
              this.$store.state.host +
              "teaforences/tags?tagname=" +
              this.teaforence.tags_2 +
              "&teaId=" +
              res.data.data.id
          })
            .then(res => {
              console.log("添加二级tag");
              console.log(res);
            })
            .catch(error => {
              this.$store.commit("response", error);
            });
          if (this.teaforenceForm.type == "SHARE") {
            //console.log(this.teaforence.lecturers);
            this.axios({
              // 添加讲师
              method: "post",
              url:
                this.$store.state.host + "applies/teaers/" + res.data.data.id,
              data: this.teaforence.lecturers
            })
              .then(res => {})
              .catch(error => {
                this.$store.commit("response", error);
                // alert("讲师不存在");
              });
          }
          this.loading = false;
          this.commit = false;
          //this.$router.go(0);
        })
        .catch(error => {
          this.$store.commit("response", error);
          this.loading = false;
        });
    },
    Preview_image() {
      this.fileUrl = URL.createObjectURL(this.imgFile);
    },
    async uploadImg() {
      var originalFile = new FormData();
      originalFile.append("file", this.imgFile);
      await this.axios({
        method: "post",
        url: this.$store.state.host + "resources/teaforence/",
        data: originalFile,
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
          "Content-Type": "multipart/form-data"
        }
      })
        .then(res => {
          this.teaforenceForm.picture = res.data.data;
          // alert("upload成功！" + res.data.data);
          // console.log(this.teaforenceForm.picture);
        })
        .catch(error => {
          this.$store.commit("response", error);
        });
    },
    zoomPreview() {
      if (this.showZoom == 0) {
        alert(showZoom);
        this.showZoom = 1;
      } else {
        this.showZoom = 0;
      }
    }
  }
};
</script>

<style scoped>
.rowC {
  display: flex;
  flex-direction: row;
}
.center {
  display: flex;
  justify-content: center;
}
.colC {
  display: flex;
  align-items: center;
}
.rcC {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.counter {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  color: grey lighten-3;
}
</style>
