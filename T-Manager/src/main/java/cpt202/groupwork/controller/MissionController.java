package cpt202.groupwork.controller;


import cpt202.groupwork.Response;
import cpt202.groupwork.dto.MissionDTO;
import cpt202.groupwork.dto.TodoDTO;

import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Mission;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.GanttRepository;
import cpt202.groupwork.repository.MissionRepository;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mission")
public class MissionController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TodolistRepository todolistRepository;

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MissionRepository missionRepository;

  @Autowired
  GanttRepository ganttRepository;

  @PostMapping("/add")
  @Operation(summary = "通过 ganttId 和 missionDTO 添加 mission")
  public Response<?> postMission(@Valid @RequestBody MissionDTO missionDTO) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Integer ganttId = missionDTO.getGanttId();
    Optional<Gantt> gantt = ganttRepository.findById(ganttId);
    Mission mission = new Mission();
    BeanUtils.copyProperties(missionDTO, mission);
    //mission.setMissionProgress(0);
    //mission.setMissionDuration(1);
    mission.setGanttId(ganttId);
    gantt.get().setGanttTotalNum(gantt.get().getGanttTotalNum() + 1);
    ganttRepository.save(gantt.get());
    missionRepository.save(mission);
    return Response.ok();
  }

  @DeleteMapping("/delete/{missionId}")
  @Operation(summary = "删除mission")
  public Response<?> deleteMission(@PathVariable Long missionId) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Optional<Mission> mission = missionRepository.findById(missionId);
    Gantt gantt = ganttRepository.findById(mission.get().getGanttId()).get();
//    if (todo.isEmpty()) {
//      return Response.ok();
//    }
    //只有自己才能删除自己的todo的限制
//    if (!username.get().equals(subDiscussion.get().getUsername())) {
//      return TeaInfo.permissionDenied("只有自己才能删除哦！");
//    }
    gantt.setGanttTotalNum(gantt.getGanttTotalNum() - 1);
    ganttRepository.save(gantt);
    missionRepository.deleteById(missionId);
    return Response.ok();
  }

  @PutMapping("/edit")
  @Operation(summary = "修改mission信息")
  public Response<?> putMission(@Valid @RequestBody Mission missionInfo) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
    // 没有登陆
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Long missionId = missionInfo.getMissionId();
    Optional<Mission> mission = missionRepository.findById(missionId);

//    if (mission.isEmpty()) {
//      return Response.notFound("没有找到todo哦！");
//    }
    BeanUtils.copyProperties(missionInfo, mission.get());
    missionRepository.save(mission.get());
    return Response.ok();
  }



  @GetMapping("/get/{missionId}")
  @Operation(summary = "查看mission详情")
  public Response<?> getMission(@PathVariable Long missionId) {
    Optional<Mission> mission = missionRepository.findById(missionId);
//  UserSelfVO userSelfVO = new UserSelfVO();
//  BeanUtils.copyProperties(user.get(), userSelfVO);
    return Response.ok(mission);
  }

}
