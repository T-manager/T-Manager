package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodolistDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
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
 * @className: TodolistController
 * @description: Controller layer for the todolist module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

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

  /**
   * add todolist by projectId and todolistcreateDTO
   * @param todolistDTO
   * @return response
   */
  @PostMapping("/add")
  @Operation(summary = "add todolist by projectId and todolistcreateDTO")
  public Response<?> createTodolist(@Valid @RequestBody TodolistDTO todolistDTO) {

    Todolist todolist = new Todolist();
    BeanUtils.copyProperties(todolistDTO, todolist);
    todoListRepository.save(todolist);
    return Response.ok(todolist);
  }

  /**
   * @param todolistId
   * @return response
   */
  @DeleteMapping("/delete/{todolistId}")
  @Operation(summary = "delete todolist")
  public Response<?> deleteTodolist(@PathVariable Integer todolistId) {
    Optional<Todolist> todolist = todoListRepository.findById(todolistId);
    todoListRepository.deleteById(todolistId);
    return Response.ok();
  }

  /**
   * @param todolist
   * @return response
   */
  @PutMapping("/modify")
  @Operation(summary = "modify information todo")
  public Response<?> modifyTodolist(@Valid @RequestBody Todolist todolist) {
    Optional<Todolist> todolistOld = todoListRepository.findById(todolist.getTodolistId());

    BeanUtils.copyProperties(todolist, todolistOld.get());
    todoListRepository.save(todolistOld.get());
    return Response.ok("modify success");
  }

  /**
   * @param projectId
   * @return response
   */
  @GetMapping("/get/{projectId}")
  @Operation(summary = "get all todolists and following todos by projectId")
  public Response<?> getTodolist(@PathVariable Integer projectId) {
    List<TodolistViewDTO> todolistViewDTOs = new ArrayList<>();
    todolistViewDTOs = todolistService.getTodolist(projectId);
    return Response.ok(todolistViewDTOs);
  }
}
