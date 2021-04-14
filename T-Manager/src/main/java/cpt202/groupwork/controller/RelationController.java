package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.RelationDTO;
import cpt202.groupwork.entity.relation.ProjectMember;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.RelationRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
import cpt202.groupwork.service.RelationService;
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
    public Response<?> addRelation(@Valid @RequestBody RelationDTO RelationDTO) {
        ProjectMember projectmember = new ProjectMember();
        BeanUtils.copyProperties(RelationDTO, projectmember);
        relationRepository.save(projectmember);
        return Response.ok(projectmember);
    }

    @DeleteMapping("/delete/{projectMemberId}")
    @Operation(summary = "delete a relation")
    public Response<?> deleteRelation(@PathVariable Integer projectMemberId) {

        Optional<ProjectMember> projectMember = relationRepository.findById(projectMemberId);

        relationRepository.deleteById(projectMemberId);
        return Response.ok();
    }

    @GetMapping("getproject/{username}")
    @Operation(summary = "get all project infomation belong to a user")
    public Response<?> getUserProject(@PathVariable String username) {
        return Response.ok(relationService.getUserProject(username));
    }

    @GetMapping("getuser/{projectId}")
    @Operation(summary = "get all user infomation under a project")
    public Response<?> getProjectUser(@PathVariable Integer projectId) {
        return Response.ok(relationService.getProjectUser(projectId));
    }
}

