package cpt202.groupwork.controller;


import cpt202.groupwork.Response;
import cpt202.groupwork.dto.MissionDTO;
import cpt202.groupwork.dto.TodoDTO;

import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Mission;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.entity.User;
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
  @Operation(summary = "add mission with ganttId and missionDTO ")
  public Response<?> createMission(@Valid @RequestBody MissionDTO missionDTO) {

    Integer ganttId = missionDTO.getGanttId();
    Optional<Gantt> gantt = ganttRepository.findById(ganttId);

    if (gantt.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "gantt does not exist");
    }
    int nameLength = missionDTO.getMissionName().length();
    if (nameLength < 1 || nameLength > 20) {
      return Response.exceptionHandling(342, "mission name should be between 1 and 20 characters");
    }

    Mission mission = new Mission();
    BeanUtils.copyProperties(missionDTO, mission);

    mission.setGanttId(ganttId);
    gantt.get().setGanttTotalNum(gantt.get().getGanttTotalNum() + 1);
    ganttRepository.save(gantt.get());
    missionRepository.save(mission);
    return Response.ok();
  }

  @DeleteMapping("/delete/{missionId}")
  @Operation(summary = "delete mission")
  public Response<?> deleteMission(@PathVariable Long missionId) {

    Optional<Mission> mission = missionRepository.findById(missionId);

    if (mission.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "mission does not exist");
    }
    Gantt gantt = ganttRepository.findById(mission.get().getGanttId()).get();
    gantt.setGanttTotalNum(gantt.getGanttTotalNum() - 1);
    ganttRepository.save(gantt);
    missionRepository.deleteById(missionId);
    return Response.ok();
  }

  @PutMapping("/edit")
  @Operation(summary = "modify mission information")
  public Response<?> putMission(@Valid @RequestBody Mission missionInfo) {

    Long missionId = missionInfo.getMissionId();
    Optional<Mission> mission = missionRepository.findById(missionId);

    if (mission.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "mission does not exist");
    }
    int nameLength = missionInfo.getMissionName().length();
    if (nameLength < 1 || nameLength > 20) {
      return Response.exceptionHandling(342, "mission name should be between 1 and 20 characters");
    }

    BeanUtils.copyProperties(missionInfo, mission.get());
    missionRepository.save(mission.get());
    return Response.ok();
  }



  @GetMapping("/get/{missionId}")
  @Operation(summary = "get mission information")
  public Response<?> getMission(@PathVariable Long missionId) {
    Optional<Mission> mission = missionRepository.findById(missionId);
    if (mission.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "mission does not exist");
    }
    return Response.ok(mission);
  }

}
