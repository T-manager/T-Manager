package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.GanttDTO;
//import cpt202.groupwork.dto.TodolistDTO;
//import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.dto.GanttViewDTO;
import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.GanttRepository;
import cpt202.groupwork.repository.MissionRepository;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
import cpt202.groupwork.service.GanttService;
import cpt202.groupwork.service.TodolistService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
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

/**
 * @className: GanttController
 * @description: Controller layer for the gantt module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/gantt")
public class GanttController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  GanttRepository ganttRepository;

  @Autowired
  MissionRepository missionRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GanttService ganttService;

  /**
   * @param ganttDTO
   * @return response
   */
  @PostMapping("/add")
  @Operation(summary = "use projectId and granttDTO to add gantt")
  public Response<?> createGantt(@Valid @RequestBody GanttDTO ganttDTO) {
    //ganttId will generate by increment
    Gantt gantt = new Gantt();
    BeanUtils.copyProperties(ganttDTO, gantt);
    ganttRepository.save(gantt);
    return Response.ok(gantt);
  }

  /**
   * @param ganttId
   * @return response
   */
  @DeleteMapping("/delete/{ganttId}")
  @Operation(summary = "delete gantt chart")
  public Response<?> deleteDiscussion(@PathVariable Integer ganttId) {
    Optional<Gantt> gantt = ganttRepository.findById(ganttId);
    ganttRepository.deleteById(ganttId);
    return Response.ok();
  }

  /**
   * @param gantt
   * @return response
   */
  @PutMapping("/modify")
  @Operation(summary = "modify information for gantt")
  public Response<?> modifyProject(@Valid @RequestBody Gantt gantt) {
    Optional<Gantt> ganttOld = ganttRepository.findById(gantt.getGanttId());

    BeanUtils.copyProperties(gantt, ganttOld.get());
    ganttRepository.save(ganttOld.get());
    return Response.ok("modify success");
  }

  /**
   * @param projectId
   * @return response
   */
  @GetMapping("/get/{projectId}")
  @Operation(summary = "get all gantt and following mission by projectid")
  public Response<?> getDiscussion(@PathVariable Integer projectId) {
    List<GanttViewDTO> ganttViewDTOs = new ArrayList<>();
    ganttViewDTOs = ganttService.getGantt(projectId);
    if(ganttViewDTOs.size() == 0) {
      GanttViewDTO ganttViewDTO = new GanttViewDTO();
      ganttViewDTO.setProjectId(projectId);
      ganttViewDTO.setGanttName("new Gantt");
      ganttViewDTO.setGanttTotalNum(0);
      Gantt newGantt = new Gantt();
      BeanUtils.copyProperties(ganttViewDTO, newGantt);
      ganttRepository.save(newGantt);
      ganttViewDTOs.add(ganttViewDTO) ;
    }

    return Response.ok(ganttViewDTOs);
  }
}
