package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.ProjectDTO;
import cpt202.groupwork.dto.ProjectDetailDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.entity.relation.ProjectMember;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.RelationRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.ProjectService;
import cpt202.groupwork.service.RelationService;
//import cpt202.groupwork.security.SecurityUtils;
import cpt202.groupwork.service.impl.RelationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.*;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project")
public class ProjectController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  RelationRepository relationRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ProjectService projectService;

  @PostMapping("/add")
  @Operation(summary = "add a project")
  public Response<?> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
    Project project = new Project();
    BeanUtils.copyProperties(projectDTO, project);
    project.setProjectOwnerId(userRepository.findByUserName(projectDTO.getProjectOwner()).get().getUserId());
    projectRepository.save(project);
    ProjectMember pm = new ProjectMember();
    pm.setProjectId(project.getProjectId());
    pm.setMemberId(project.getProjectOwnerId());
    pm.setMemberRole("owner");
    relationRepository.save(pm);
    return Response.ok();
  }

  @DeleteMapping("/delete/{projectId}")
  @Operation(summary = "delete a project")
  public Response<?> deleteProject(@PathVariable Integer projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    List<ProjectMember> pms = relationRepository.findByProjectId(projectId);
    for (ProjectMember pm : pms) {
      relationRepository.delete(pm);
    }
    projectRepository.deleteById(projectId);

    return Response.ok();
  }

  @PutMapping("/modify")
  @Operation(summary = "modify the information of a project")
  public Response<?> modifyProject(@Valid @RequestBody Project projectInfo) {
    Integer projectId = projectInfo.getProjectId();
    Optional<Project> project = projectRepository.findById(projectId);
    projectInfo.setProjectOwnerId(project.get().getProjectOwnerId());
    BeanUtils.copyProperties(projectInfo, project.get());
    return Response.ok(projectRepository.save(project.get()));
  }

  @GetMapping("/get/{projectId}")
  @Operation(summary = "get the infomation of project")
  public Response<?> getProjectV2(@PathVariable Integer projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    ProjectDTO projectDTO = new ProjectDTO();
    BeanUtils.copyProperties(project.get(), projectDTO);
    return Response.ok(project);
  }

  @GetMapping("/getV2/{projectId}")
  @Operation(summary = "get the infomation of project")
  public Response<?> getProject(@PathVariable Integer projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    ProjectDTO projectDTO = new ProjectDTO();
    BeanUtils.copyProperties(project.get(), projectDTO);
    projectDTO.setProjectOwner(userRepository.findById(project.get().getProjectOwnerId()).get().getUserName());
    return Response.ok(projectDTO);
  }

  @GetMapping("/search/{userName}/{namePattern}")
  @Operation(summary = "search the project of a user by certain rule")
  public Response<?> searchProject(@PathVariable String userName, @PathVariable String namePattern) {
    return Response.ok(projectService.searchUserProject(userName,namePattern));
  }

}
