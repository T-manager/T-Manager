<template>
  <div style="padding: 10px 70px 0px 70px">
    <div style="font-size: 20px; color: red" class="app-container">
      <label><input type="radio" name="scale" value="day" checked/>day scale</label>
      <label><input type="radio" name="scale" value="week" />week scale</label>
      <label><input type="radio" name="scale" value="month"/>month scale</label>
      <label><input type="radio" name="scale" value="year"/>year scale</label>
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
  methods: {
    updateMission() {
      //update mission
      var that = this;
      gantt.attachEvent("onAfterTaskUpdate", function(id, task) {
        // alert("has updated!");
        var formatFunc = gantt.date.date_to_str("%Y-%m-%d %H:%m:%s");
        var date = formatFunc(task.start_date); // date format
        that
          .$axios({
            method: "put",
            url: that.$store.state.host + "mission/edit",
            data: {
              missionId: task.id,
              ganttId: that.gantt.ganttId,
              missionProgress: task.progress,
              missionName: task.text,
              missionStart: date,
              missionDuration: task.duration,
              missionParent: task.parent
            },
            headers: {
              Authorization: "Bearer " + that.$store.getters.getToken
            }
          })
          .then(res => {
            // console.log("edit", res, task.id);
          })
          .catch(error => {
            that.$store.commit("response", error);
            //this.loadAddTodoList = false;
          });
      });
    },
    deleteMission() {
      var that = this;
      gantt.attachEvent("onBeforeTaskDelete", function(id, item) {
        // console.log(gantt.getTask(id).id);
        that
          .$axios({
            method: "delete",
            url: that.$store.state.host + "mission/delete/" + id,
            headers: {
              Authorization: "Bearer " + that.$store.getters.getToken
            }
          })
          .then(res => {
            // console.log("delete", res, id);
            //that.$router.go(0);
          })
          .catch(error => {
            that.$store.commit("response", error);
          });
        return true;
      });
    }
  },
  mounted() {
    //gantt.config.autofit = true;
    gantt.config.grid_width = 500;
    gantt.config.xml_date = "%Y-%m-%d";

    // 在时间线上增加一行年份显示
    //gantt.config.subscales = [{ unit: "year", step: 1, date: "%Y" }];
    gantt.templates.grid_header_class = function(columnName, column) {
      // console.log(columnName)
      // console.log(column)
      return "updColor";
    };
    
    //#################   text filter start   #################
    
    var filterValue = "";
    gantt.$doFilter = function(value){
      filterValue = value;
      gantt.refreshData();
    }

    gantt.attachEvent("onBeforeTaskDisplay", function(id, task){
      if(!filterValue) return true;

      var normalizedText = task.text.toLowerCase();
      var normalizedValue = filterValue.toLowerCase();
      return normalizedText.indexOf(normalizedValue) > -1;
    });

    gantt.attachEvent("onGanttRender", function(){
      gantt.$root.querySelector("[data-text-filter]").value = filterValue;
    })

    var textFilter = "<div class='searchEl'>Mission <input data-text-filter id='search' type='text' placeholder='Search Mission' oninput='gantt.$doFilter(this.value)'></div>";
    gantt.config.columns = [
        {name: "text", label: textFilter, tree: true, width: 200, resize: true},
        { name: "start_date", label: "Start time", align: "center", width:"*"},
        { name: "duration", label: "Duration", align: "center", width:"*" },
        { name: "add", label: "", width: 40 },
      ];

  
    //#################   text filter end   ################
   
    //#################   setScale start   #################
     var zoomConfig = {
          levels: [
            {
              name:"day",
              scale_height: 35,
              min_column_width:80,
              scales:[
                  {unit: "day", step: 1, format: "%d %M"}
              ]
            },
            {
              name:"week",
              scale_height: 35,
              min_column_width:50,
              scales:[
                {unit: "week", step: 1, format: function (date) {
                var dateToStr = gantt.date.date_to_str("%d %M");
                var endDate = gantt.date.add(date, 6, "day");
                var weekNum = gantt.date.date_to_str("%W")(date);
                return "#" + weekNum + ", " + dateToStr(date) + " - " + dateToStr(endDate);
                }},
                {unit: "day", step: 1, format: "%j %D"}
              ]
            },
            {
              name:"month",
              scale_height: 35,
              min_column_width:120,
              scales:[
                  {unit: "month", format: "%F, %Y"},
                  {unit: "week", format: "Week #%W"}
              ]
              },
              {
              name:"quarter",
              height: 35,
              min_column_width:90,
              scales:[
                {unit: "month", step: 1, format: "%M"},
                {
                unit: "quarter", step: 1, format: function (date) {
                  var dateToStr = gantt.date.date_to_str("%M");
                  var endDate = gantt.date.add(gantt.date.add(date, 3, "month"), -1, "day");
                  return dateToStr(date) + " - " + dateToStr(endDate);
                }
              }
              ]},
              {
                name:"year",
                scale_height: 35,
                min_column_width: 30,
                scales:[
                  {unit: "year", step: 1, format: "%Y"}
              ]}
          ]
      };
 
      gantt.ext.zoom.init(zoomConfig);

       function setScaleConfig(level) {
        switch (level) {
            case "day":
                gantt.config.scales = [
                    {unit: "day", step: 1, format: "%d %M"}
                ];
                gantt.config.scale_height = 35;
                break;
            case "week":
                var weekScaleTemplate = function (date) {
                  var dateToStr = gantt.date.date_to_str("%d %M");
                  var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
                  return dateToStr(date) + " - " + dateToStr(endDate);
                };
                gantt.config.scales = [
                    {unit: "week", step: 1, format: weekScaleTemplate},
                    {unit: "day", step: 1, format: "%D"}
                ];
                gantt.config.scale_height = 35;
                break;
            case "month":
                var weekScaleTemplate = function (date) {
                  var dateToStr = gantt.date.date_to_str("%d %M");
                  var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
                  return dateToStr(date) + " - " + dateToStr(endDate);
                };
                gantt.config.scales = [
                    {unit: "month", step: 1, format: "%F, %Y"},
                    {unit: "week", step: 1, format: weekScaleTemplate},
                ];
                gantt.config.scale_height = 35;
                break;
            case "year":
                gantt.config.scales = [
                    {unit: "year", step: 1, format: "%Y"},
                    {unit: "month", step: 1, format: "%M"}
                ];
                gantt.config.scale_height = 35;
                break;
          }
      }

      //setScaleConfig("year");

      var els = document.querySelectorAll("input[name='scale']");
      for (var i = 0; i < els.length; i++) {
          els[i].onclick = function(e){
              var el = e.target;
              var value = el.value;
              setScaleConfig(value);
              gantt.render();
          };
      }

  
     //#################   scale end   #################

    function deleteDemo(e) {
      $.post(
        "${ctx}/ganttlinks/ganttLinks/delete",
        e,
        function(json) {
          // console.log(json.status);
        },
        "json"
      );
    }
    gantt.config.show_links = false;
    gantt.locale.labels.section_description = "Mission name";
    gantt.config.scroll_size = 20;
    gantt.config.fit_tasks = true;
    gantt.config.buttons_left = ["gantt_delete_btn"];
    gantt.config.buttons_right = ["gantt_save_btn", "gantt_cancel_btn"];
    gantt.config.sort = true;
  


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
        // console.log("get all", res);
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
              parent: mission.missionParent
            };
            var taskId = gantt.addTask(newTask);
            // console.log(gantt.getTask(taskId));
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
    gantt.attachEvent("onLightboxSave", function(id, task, is_new) {
      if(task.text.length > 20 || task.text.length <= 0) {

        gantt.alert("The length of name should be 1-20");
        return false;
      }
      else if (is_new) {
        var formatFunc = gantt.date.date_to_str("%Y-%m-%d %H:%m:%s");
        var date = formatFunc(task.start_date); // date format
        that
          .$axios({
            method: "post",
            url: that.$store.state.host + "mission/add",
            data: {
              missionId: id,
              ganttId: that.gantt.ganttId,
              missionProgress: task.progress,
              missionName: task.text,
              missionStart: date,
              missionDuration: task.duration,
              missionParent: task.parent
            },
            headers: {
              Authorization: "Bearer " + that.$store.getters.getToken
            }
          })
          .then(res => {})
          .catch(error => {
            that.$store.commit("response", error);
          });
      }
      return true;
    });
    //drag child mission
    gantt.attachEvent("onTaskDrag", function(id, mode, task, original) {
      var modes = gantt.config.drag_mode;
      if (mode == modes.move) {
        var diff = task.start_date - original.start_date;
        gantt.eachTask(function(child) {
          child.start_date = new Date(+child.start_date + diff);
          child.end_date = new Date(+child.end_date + diff);
          gantt.refreshTask(child.id, true);
        }, id);
      }
    });
    //rounds positions of the child items to scale
    gantt.attachEvent("onAfterTaskDrag", function(id, mode, e) {
      var modes = gantt.config.drag_mode;
      if (mode == modes.move) {
        var state = gantt.getState();
        gantt.eachTask(function(child) {
          child.start_date = gantt.roundDate({
            date: child.start_date,
            unit: state.scale_unit,
            step: state.scale_step
          });
          child.end_date = gantt.calculateEndDate(
            child.start_date,
            child.duration,
            gantt.config.duration_unit
          );
          gantt.updateTask(child.id);
        }, id);
      }
    });
    //update Mission
    this.updateMission();
    //delete mission
    this.deleteMission();
  },
  created() {
    if (this.$store.getters.getToken == null) {
      alert("You are not signned in yet!");
      var path = "/login";
      this.$router.push({ path: path });
    }
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
  height: 730px;
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
  background: #283593 !important;
}
.gantt_grid_data .gantt_row.odd:hover,
.gantt_grid_data .gantt_row:hover,
.gantt_grid_data .gantt_row.gantt_selected,
.gantt_grid_data .gantt_row.odd.gantt_selected,
.gantt_task_row.gantt_selected {
  background-color: #b3e5fc !important;
}

.gantt_grid_head_cell.gantt_grid_head_duration.updColor {
  font: 20px Helvetica;
  color: #f1ecec;
  padding: 5px;
}

.gantt_grid_head_cell.gantt_grid_head_start_date.updColor {
  font: 20px Helvetica;
  color: #f1ecec;
  padding: 5px;
}
.gantt_grid_head_cell.gantt_grid_head_text.updColor {
  font: 20px Helvetica;
  color: #f1ecec;
  padding: 4px;
}

#search{
  width:130px;
  margin-left:3px;
  padding:4px 3px 3px;
  font-size:12px;  
  background-color:	#c5cae9;
}
.searchEl:after{
  display:block;
  content: '';
  height:30px;
  margin-top:-28px;
  margin-left:74px;
}
label{
  color: #283593;
}

</style>
