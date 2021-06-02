package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.GanttDTO;
//import cpt202.groupwork.dto.TodolistDTO;
//import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.dto.GanttViewDTO;
import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Project;
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

  @PostMapping("/add")
  @Operation(summary = "通过 projectId 和 ganttDTO 添加 gantt")
  public Response<?> createGantt(@Valid @RequestBody GanttDTO ganttDTO) {
    Optional<Project> project = projectRepository.findById(ganttDTO.getProjectId());
    // check if project exist
    if (project.equals(Optional.empty())) {
      return Response.exceptionHandling(302, "project does not exist");
    }
    //ganttId会自动按顺序生成
    Gantt gantt = new Gantt();
    BeanUtils.copyProperties(ganttDTO, gantt);
    ganttRepository.save(gantt);
    return Response.ok();
  }

  @DeleteMapping("/delete/{ganttId}")
  @Operation(summary = "删除gantt")
  public Response<?> deleteGantt(@PathVariable Integer ganttId) {
    return ganttService.deleteGantt(ganttId);
  }

  @PutMapping("/modify")
  @Operation(summary = "修改gantt信息")
  public Response<?> modifyProject(@Valid @RequestBody Gantt gantt) {
    Optional<Gantt> ganttOld = ganttRepository.findById(gantt.getGanttId());
    if (ganttOld.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "gantt does not exist");
    }
    BeanUtils.copyProperties(gantt, ganttOld.get());
    ganttRepository.save(ganttOld.get());
    return Response.ok("modify success");
  }


  @GetMapping("/get/{projectId}")
  @Operation(summary = "通过 projectid 查看所有的 gantt, 包括所属的mission")
  public Response<?> getDiscussion(@PathVariable Integer projectId) {
    return ganttService.getGantt(projectId);
  }
}
