<template>
  <div style="padding: 35px">
    <h2 style="text-align: center">Gantt echart</h2>
    <div style="font-size: 20px; color: red" class="app-container">
      <div ref="gantt" class="left-container" />
    </div>
  </div>
</template>

<script>
import gantt from "dhtmlx-gantt"; // 引入模块
import "dhtmlx-gantt/codebase/dhtmlxgantt.css";
// import 'dhtmlx-gantt/codebase/locale/locale_cn'  // 本地化
export default {
  name: "ganttEchart",
  data() {
    return {
      projectId: 0,
      tasks: {
        data: [],
        links: []
      }
    };
  },

  mounted() {
    gantt.config.autofit = true;
    gantt.config.grid_width = 600;
    gantt.config.xml_date = "%Y-%m-%d";
    // 在时间线上增加一行年份显示
    gantt.config.subscales = [{ unit: "year", step: 1, date: "%Y" }];
    gantt.templates.grid_header_class = function(columnName, column) {
      // console.log(columnName)
      // console.log(column)
      return "updColor";
    };

    function deleteDemo(e) {
      $.post(
        "${ctx}/ganttlinks/ganttLinks/delete",
        e,
        function(json) {
          console.log(json.status);
        },
        "json"
      );
    }
    gantt.config.show_links = false;
    gantt.locale.labels.section_description = "Mission name";

    gantt.config.columns = [
      {
        name: "text",
        label: "Mission name",
        width: "*",
        tree: true,
        resize: true
      },
      { name: "start_date", label: "Start time", align: "center", width: 90 },
      { name: "duration", label: "Duration", align: "center" },
      { name: "add", label: "", width: 44 }
    ];

    gantt.templates.grid_row_class = function(start, end, task) {
      if (task.$level >= 0) {
        return "nested_task";
      }
      return "";
    };

    gantt.config.scroll_size = 20;
    gantt.config.fit_tasks = true;

    // 初始化
    gantt.init(this.$refs.gantt);
    // 数据解析
    gantt.parse(this.tasks);
    // #E8EAF6
    this.projectId = this.$route.path.split("projectid=")[1];
    //加载全部mission
    this.$axios({
      method: "get",
      url: this.$store.state.host + "gantt/get/" + this.projectId,
      headers: {
        Authorization: "Bearer " + this.$store.getters.getToken
      }
    })
      .then(res => {
        console.log("get all", res);
        if (res.data.data.length == 0) this.gantt = {};
        else {
          this.gantt = res.data.data[0];
          for (var i in this.gantt.missionViewDTO) {
            this.gantt.missionViewDTO[i].loading = false;
            var mission = this.gantt.missionViewDTO[i];
            var newTask = {
              id: mission.missionId,
              text: mission.missionName,
              start_date: mission.missionStart,
              duration: mission.missionDuration,
              progress: mission.missionProgress,              
            };
            var taskId = gantt.addTask(newTask);
            console.log(gantt.getTask(taskId));
            gantt.getTask(taskId).id = taskId;
            gantt.updateTask(newTask.id); //renders the updated task
          }
        }
      })
      .catch(error => {
        this.$store.commit("response", error);
      });

    var that = this;
    //add mission
    gantt.attachEvent("onLightboxSave", function(id, task, is_new) {
      if (is_new) {
        var formatFunc = gantt.date.date_to_str("%Y-%m-%d %H:%m:%s");
        var date = formatFunc(task.start_date); // date format
        that
          .$axios({
            method: "post",
            url: that.$store.state.host + "mission/add",
            data: {
              missionId: id,
              ganttId: 0,
              missionProgress: task.progress,
              missionName: task.text,
              missionStart: date,
              missionDuration: task.duration,           
            },
            headers: {
              Authorization: "Bearer " + that.$store.getters.getToken
            }
          })
          .then(res => {
            console.log("add", res, task.id);
          })
          .catch(error => {
            that.$store.commit("response", error);
          });
      }
      return true;
    });

    //update mission
    gantt.attachEvent("onAfterTaskUpdate", function(id, task) {
      var formatFunc = gantt.date.date_to_str("%Y-%m-%d %H:%m:%s");
      var date = formatFunc(task.start_date); // date format
      that
        .$axios({
          method: "put",
          url: that.$store.state.host + "mission/edit",
          data: {
            missionId: task.id,
            ganttId: 0,
            missionProgress: task.progress,
            missionName: task.text,
            missionStart: date,
            missionDuration: task.duration,
          },
          headers: {
            Authorization: "Bearer " + that.$store.getters.getToken
          }
        })
        .then(res => {
          console.log("edit", res, task.id);
        })
        .catch(error => {
          that.$store.commit("response", error);
          //this.loadAddTodoList = false;
        });
    });

    //delete mission
    gantt.attachEvent("onBeforeTaskDelete", function(id, item) {
      console.log(gantt.getTask(id).id);
      that
        .$axios({
          method: "delete",
          url: that.$store.state.host + "mission/delete/" + id,
          headers: {
            Authorization: "Bearer " + that.$store.getters.getToken
          }
        })
        .then(res => {
          console.log("delete", res, id);
          //that.$router.go(0);
        })
        .catch(error => {
          that.$store.commit("response", error);
        });
      return true;
    });
  }
};
</script>

<style>
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}
.left-container {
  height: 600px;
}
.updColor {
  background-color: #5c6bc0 !important;
}

.gantt_task_line {
  background-color: #c5cae9 !important;
}
.gantt_task_progress {
  background-color: #5c6bc0 !important;
}

.gantt_btn_set.gantt_left_btn_set.gantt_save_btn_set {
  background-color: #9fa8da;
}

.gantt_btn_set.gantt_left_btn_set.gantt_delete_btn_set {
  background-color: #283593;
}

.gantt_btn_set.gantt_left_btn_set.gantt_cancel_btn_set {
  background-color: #e8eaf6;
}

.gantt_popup_button.gantt_ok_button {
  background: #283593;
}
.gantt_grid_data .gantt_row.odd:hover,
.gantt_grid_data .gantt_row:hover,
.gantt_grid_data .gantt_row.gantt_selected,
.gantt_grid_data .gantt_row.odd.gantt_selected,
.gantt_task_row.gantt_selected {
  background-color: #b3e5fc;
}

.nested_task .gantt_add {
  display: none !important;
}

.gantt_grid_head_cell.gantt_grid_head_duration.updColor {
  font: 20px Helvetica;
  color: #f1ecec;
}
.gantt_grid_head_cell.gantt_grid_head_start_date.updColor {
  font: 20px Helvetica;
  color: #f1ecec;
}
.gantt_grid_head_cell.gantt_grid_head_text.updColor {
  font: 20px Helvetica;
  color: #f1ecec;
}
</style>
