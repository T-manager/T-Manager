package cpt202.groupwork.controller;


import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodoDTO;

import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
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

/**
 * @className: TodoController
 * @description: Controller layer for the todo module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/todo")
public class TodoController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TodolistRepository todolistRepository;

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  /**
   * create a todo
   * @param todoDTO
   * @return response
   */
  @PostMapping("/add")
  @Operation(summary = "Add todo with todoListId and todoCreateDTO")
  public Response<?> createTodo(@Valid @RequestBody TodoDTO todoDTO) {
    Integer todolistId = todoDTO.getTodolistId();
    Optional<Todolist> todolist = todolistRepository.findById(todolistId);
    Todo todo = new Todo();
    BeanUtils.copyProperties(todoDTO, todo);
    todo.setTodoCheck(false);
    todo.setTodolistId(todolistId);
    todolist.get().setTodolistTotalNum(todolist.get().getTodolistTotalNum() + 1);
    todolistRepository.save(todolist.get());
    todoRepository.save(todo);
    return Response.ok();
  }

  /**
   * delete a todo by its id
   * @param todoId
   * @return response
   */
  @DeleteMapping("/delete/{todoId}")
  @Operation(summary = "delete todo")
  public Response<?> deleteTodo(@PathVariable Integer todoId) {
    Optional<Todo> todo = todoRepository.findById(todoId);
    Todolist todolist = todolistRepository.findById(todo.get().getTodolistId()).get();
    todolist.setTodolistTotalNum(todolist.getTodolistTotalNum() - 1);
    if (todo.get().getTodoCheck() == true) {
      todolist.setTodolistCompleteNum(todolist.getTodolistCompleteNum() - 1);
    }
    todolistRepository.save(todolist);
    todoRepository.deleteById(todoId);
    return Response.ok();
  }

  /**
   * modify infoamtion of todo
   * @param todoInfo
   * @return response
   */
  @PutMapping("/edit")
  @Operation(summary = "modify information of todo")
  public Response<?> modifyTodo(@Valid @RequestBody Todo todoInfo) {
    Integer todoId = todoInfo.getTodoId();
    Optional<Todo> todo = todoRepository.findById(todoId);
    BeanUtils.copyProperties(todoInfo, todo.get());
    todoRepository.save(todo.get());
    return Response.ok();
  }

  /**
   * check state of the todo
   * @param todoId
   * @return response
   */
  @PutMapping("/check/{todoId}")
  @Operation(summary = "finish Todo")
  public Response<?> checkTodo(@PathVariable Integer todoId) {
    Optional<Todo> todo = todoRepository.findById(todoId);
    Optional<Todolist> todolist = todolistRepository.findById(todo.get().getTodolistId());
    //if it is unfinished to-do
    if (todo.get().getTodoCheck() == false) {
      todolist.get().setTodolistCompleteNum(todolist.get().getTodolistCompleteNum() + 1);
    } else {
      todolist.get().setTodolistCompleteNum(todolist.get().getTodolistCompleteNum() - 1);
    }
    todo.get().setTodoCheck(!todo.get().getTodoCheck());
    return Response.ok(todoRepository.save(todo.get()));
  }

  /**
   * get the detail of the todo
   * @param todoId
   * @return response
   */
  @GetMapping("/get/{todoId}")
  @Operation(summary = "get the detail of the todo")
  public Response<?> getTodo(@PathVariable Integer todoId) {
    Optional<Todo> todo = todoRepository.findById(todoId);
    return Response.ok(todo);
  }

}
