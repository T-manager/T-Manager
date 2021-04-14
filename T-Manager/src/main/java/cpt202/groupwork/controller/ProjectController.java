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
@RequestMapping("/project")
public class ProjectController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  RelationRepository relationRepository;

  @Autowired
  UserRepository userRepository;

  @PostMapping("/add")
  @Operation(summary = "add a project")
  public Response<?> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
    Project project = new Project();
    BeanUtils.copyProperties(projectDTO, project);
    projectRepository.save(project);
    ProjectMember pm = new ProjectMember();
    pm.setProjectId(project.getProjectId());
    pm.setMemberName(project.getProjectOwner());
    relationRepository.save(pm);
    return Response.ok();
  }

  @DeleteMapping("/delete/{projectId}")
  @Operation(summary = "delete a project")
  public Response<?> deleteProject(@PathVariable Integer projectId) {

    Optional<Project> project = projectRepository.findById(projectId);

    projectRepository.deleteById(projectId);
    return Response.ok();
  }

  @PutMapping("/modify")
  @Operation(summary = "modify the information of a project")
  public Response<?> putProject(@Valid @RequestBody Project projectInfo) {
    Integer projectId=projectInfo.getProjectId();
    Optional<Project> project = projectRepository.findById(projectId);
    BeanUtils.copyProperties(projectInfo, project.get());
    return Response.ok(projectRepository.save(project.get()));
  }

  @GetMapping("/get/{projectId}")
  @Operation(summary = "get the infomation of project")
  public Response<?> getProject(@PathVariable Integer projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    ProjectDTO projectDTO = new ProjectDTO();
    BeanUtils.copyProperties(project.get(), projectDTO);
    return Response.ok(project);
  }
}
