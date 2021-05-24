package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.RelationDTO;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.entity.relation.ProjectMember;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.RelationRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
import cpt202.groupwork.service.RelationService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/relation")
public class RelationController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RelationRepository relationRepository;

  @Autowired
  RelationService relationService;

  @PostMapping("/add")
  @Operation(summary = "add a relation")
  public Response<?> createRelation(@Valid @RequestBody RelationDTO relationDTO) {
    if(relationDTO.getMemberName().equals("")){
      return Response.exceptionHandling(341,"member name don't meet requirement");
    }
    Optional<User> user = userRepository.findByUserName(relationDTO.getMemberName());
    if(user.isPresent()){
      List<ProjectMember> pms = relationRepository.findByProjectId(relationDTO.getProjectId());
      if(pms.size()!=0){
        for (ProjectMember pm : pms) {
          if (pm.getMemberId() == user.get().getUserId()) {
            return Response.exceptionHandling(321,"user already exist in this project");
          }
        }
        ProjectMember projectmember = new ProjectMember();
        BeanUtils.copyProperties(relationDTO, projectmember);
        projectmember.setMemberRole("member");

        projectmember.setMemberId(user.get().getUserId());
        relationRepository.save(projectmember);
        return Response.ok(projectmember);
      }else{
        return Response.exceptionHandling(302,"project not found");
      }
    }else{
      return Response.exceptionHandling(301,"user not found");
    }
  }

  @DeleteMapping("/delete/{projectMemberId}")
  @Operation(summary = "delete a relation")
  public Response<?> deleteRelation(@PathVariable Integer projectMemberId) {
    if(relationRepository.findById(projectMemberId).isPresent()){
      relationRepository.deleteById(projectMemberId);
      return Response.ok();
    }else{
      return Response.exceptionHandling(301,"relation not found");
    }

  }

  @GetMapping("getproject/{username}")
  @Operation(summary = "get all project infomation belong to a user")
  public Response<?> getUserProject(@PathVariable String username) {
    Optional<User> user = userRepository.findByUserName(username);
    if(user.isPresent()){
      Integer userId = user.get().getUserId();
      return Response.ok(relationService.getUserProject(userId));
    }else{
      return Response.exceptionHandling(301,"user not found");
    }
  }

  @GetMapping("getuser/{projectId}")
  @Operation(summary = "get all user infomation under a project")
  public Response<?> getProjectUser(@PathVariable Integer projectId) {
    return relationService.getProjectUser(projectId);
  }
}

