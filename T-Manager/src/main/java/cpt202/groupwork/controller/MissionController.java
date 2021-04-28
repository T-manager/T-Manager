package cpt202.groupwork.controller;


import cpt202.groupwork.Response;
import cpt202.groupwork.dto.MissionDTO;

import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Mission;
import cpt202.groupwork.repository.GanttRepository;
import cpt202.groupwork.repository.MissionRepository;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
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

/**
 * @className: MissionController
 * @description: Controller layer for the mission module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

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

  /**
   * @param missionDTO
   * @return response
   */
  @PostMapping("/add")
  @Operation(summary = "add mission useing ganttId and missionDTO")
  public Response<?> createMission(@Valid @RequestBody MissionDTO missionDTO) {
    Integer ganttId = missionDTO.getGanttId();
    Optional<Gantt> gantt = ganttRepository.findById(ganttId);
    Mission mission = new Mission();
    BeanUtils.copyProperties(missionDTO, mission);
    mission.setGanttId(ganttId);
    gantt.get().setGanttTotalNum(gantt.get().getGanttTotalNum() + 1);
    ganttRepository.save(gantt.get());
    missionRepository.save(mission);
    return Response.ok();
  }

  /**
   * @param missionId
   * @return response
   */
  @DeleteMapping("/delete/{missionId}")
  @Operation(summary = "delete mission")
  public Response<?> deleteMission(@PathVariable Long missionId) {
    Optional<Mission> mission = missionRepository.findById(missionId);
    Gantt gantt = ganttRepository.findById(mission.get().getGanttId()).get();
    gantt.setGanttTotalNum(gantt.getGanttTotalNum() - 1);
    ganttRepository.save(gantt);
    missionRepository.deleteById(missionId);
    return Response.ok();
  }

  /**
   * @param missionInfo
   * @return response
   */
  @PutMapping("/edit")
  @Operation(summary = "modify mission information")
  public Response<?> putMission(@Valid @RequestBody Mission missionInfo) {
    Long missionId = missionInfo.getMissionId();
    Optional<Mission> mission = missionRepository.findById(missionId);
    BeanUtils.copyProperties(missionInfo, mission.get());
    missionRepository.save(mission.get());
    return Response.ok();
  }

  /**
   * @param missionId
   * @return response
   */
  @GetMapping("/get/{missionId}")
  @Operation(summary = "get all information about mission")
  public Response<?> getMission(@PathVariable Long missionId) {
    Optional<Mission> mission = missionRepository.findById(missionId);
    return Response.ok(mission);
  }

}
