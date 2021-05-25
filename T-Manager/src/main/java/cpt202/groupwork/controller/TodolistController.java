package cpt202.groupwork.controller;

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

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodolistDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
import cpt202.groupwork.service.TodolistService;
import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/todolist")
public class TodolistController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TodolistRepository todoListRepository;

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TodolistService todolistService;

  @PostMapping("/add")
  @Operation(summary = "通过 projectId 和 todolistCreateDTO 添加 todoList")
  public Response<?> createTodolist(@Valid @RequestBody TodolistDTO todolistDTO) {
    Optional<Project> project = projectRepository.findById(todolistDTO.getProjectId());
    // check if project exist
    if (project.equals(Optional.empty())) {
      return Response.exceptionHandling(302, "project does not exist");
    }

    // check the todolistName meet requirements
    int nameLength = todolistDTO.getTodolistName().length();
    if (nameLength < 1 || nameLength > 20) {
      return Response.exceptionHandling(341, "The TODOLIST name should be between 1 and 20 characters");
    }
    Todolist todolist = new Todolist();
    BeanUtils.copyProperties(todolistDTO, todolist);
    todoListRepository.save(todolist);
    return Response.ok();
  }

  @DeleteMapping("/delete/{todolistId}")
  @Operation(summary = "删除todolist")
  public Response<?> deleteTodolist(@PathVariable Integer todolistId) {
    return todolistService.deleteTodolist(todolistId);
  }

  @PutMapping("/modify")
  @Operation(summary = "修改todolist信息")
  public Response<?> modifyTodolist(@Valid @RequestBody Todolist todolist) {
    Optional<Todolist> todolistOld = todoListRepository.findById(todolist.getTodolistId());

    if (todolistOld.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "todolist does not exist");
    }
    // check the todolistName meet requirements
    int nameLength = todolist.getTodolistName().length();
    if (nameLength < 1 || nameLength > 20) {
      return Response.exceptionHandling(341, "The TODOLIST name should be between 1 and 20 characters");
    }
    BeanUtils.copyProperties(todolist, todolistOld.get());
    todoListRepository.save(todolistOld.get());

    return Response.ok("modify success");
  }

  @GetMapping("/get/{projectId}")
  @Operation(summary = "通过 projectid 查看所有的 todolist, 包括所属的todo")
  public Response<?> getTodolist(@PathVariable Integer projectId) {
    return todolistService.getTodolist(projectId);
  }
}
