package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.ProjectDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.repository.ProjectRepository;
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
  UserRepository userRepository;

  @PostMapping("/add")
  @Operation(summary = "添加项目")
  public Response<?> addTeaforence(@Valid @RequestBody ProjectDTO projectDTO) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return TeaInfo.unAuth();
//    }
//    if (teaDTO.getStartTime().getTime() < new Date().getTime() + 86400000) {
//      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "采茶开始时间要在距现在1天之后哦!");
//    }
    Project project = new Project();
    BeanUtils.copyProperties(projectDTO, project);
    projectRepository.save(project);
    return Response.ok(project);
  }

  @DeleteMapping("/{projectId}")
  @Operation(summary = "删除项目")
  public Response<?> deleteDiscussion(@PathVariable Integer projectId) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }

    Optional<Project> project = projectRepository.findById(projectId);
//    if (project.isEmpty()) {
//      return Response.notFound();
//    }

//    if (!username.get().equals(project.get().getProjectOwner())) {
//      return Response.permissionDenied("只有项目拥有者才能删除哦！");
//    }
    projectRepository.deleteById(projectId);
    return Response.ok(project.get());
  }

  @PutMapping("/{projeectId}")
  @Operation(summary = "修改项目信息")
  public Response<?> putProject(@Valid @RequestBody Project projectInfo) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
    // 没有登陆
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Integer projectId=projectInfo.getProjectId();
    Optional<Project> project = projectRepository.findById(projectId);
    // 使用用户名找不到用户
//    if (project.isEmpty()) {
//      return Response.notFound("没有找到该用户哦！");
//    }
    BeanUtils.copyProperties(projectInfo, project.get());
    return Response.ok(projectRepository.save(project.get()));
  }

  @GetMapping("/{projectId}")
  @Operation(summary = "查看project信息")
  public Response<?> getProject(@PathVariable Integer projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    ProjectDTO projectDTO = new ProjectDTO();
    BeanUtils.copyProperties(project.get(), projectDTO);
    return Response.ok(project);
  }
}
