package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.ProjectDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.relation.ProjectMember;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.RelationRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
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
 * @className: ProjectController
 * @description: Controller layer for the project module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

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

  /**
   * add a project
   * @param projectDTO
   * @return response
   */
  @PostMapping("/add")
  @Operation(summary = "add a project")
  public Response<?> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
    Project project = new Project();
    BeanUtils.copyProperties(projectDTO, project);
    project.setProjectOwnerId(
        userRepository.findByUserName(projectDTO.getProjectOwner()).get().getUserId());
    projectRepository.save(project);
    ProjectMember pm = new ProjectMember();
    pm.setProjectId(project.getProjectId());
    pm.setMemberId(project.getProjectOwnerId());
    pm.setMemberRole("owner");
    relationRepository.save(pm);
    return Response.ok();
  }

  /**
   * @param projectId
   * @return response
   */
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

  /**
   * @param projectInfo
   * @return response
   */
  @PutMapping("/modify")
  @Operation(summary = "modify the information of a project")
  public Response<?> modifyProject(@Valid @RequestBody Project projectInfo) {
    Integer projectId = projectInfo.getProjectId();
    Optional<Project> project = projectRepository.findById(projectId);
    projectInfo.setProjectOwnerId(project.get().getProjectOwnerId());
    BeanUtils.copyProperties(projectInfo, project.get());
    return Response.ok(projectRepository.save(project.get()));
  }

  /**
   * @param projectId
   * @return response
   */
  @GetMapping("/get/{projectId}")
  @Operation(summary = "get the infomation of project")
  public Response<?> getProject(@PathVariable Integer projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    ProjectDTO projectDTO = new ProjectDTO();
    BeanUtils.copyProperties(project.get(), projectDTO);
    return Response.ok(project);
  }
}
