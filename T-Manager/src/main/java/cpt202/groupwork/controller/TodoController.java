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

  @PostMapping("/add")
  @Operation(summary = "通过 todolistId 和 todoCreateDTO 添加 todo")
  public Response<?> postTodo(@Valid @RequestBody TodoDTO todoDTO) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
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

  @DeleteMapping("/delete/{todoId}")
  @Operation(summary = "删除todo")
  public Response<?> deleteTodo(@PathVariable Integer todoId) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Optional<Todo> todo = todoRepository.findById(todoId);
    Todolist todolist = todolistRepository.findById(todo.get().getTodolistId()).get();
//    if (todo.isEmpty()) {
//      return Response.ok();
//    }
    //只有自己才能删除自己的todo的限制
//    if (!username.get().equals(subDiscussion.get().getUsername())) {
//      return TeaInfo.permissionDenied("只有自己才能删除哦！");
//    }
    todolist.setTodolistTotalNum(todolist.getTodolistTotalNum() - 1);
    if (todo.get().getTodoCheck() == true) {
      todolist.setTodolistCompleteNum(todolist.getTodolistCompleteNum() - 1);
    }
    todolistRepository.save(todolist);
    todoRepository.deleteById(todoId);
    return Response.ok();
  }

  @PutMapping("/")
  @Operation(summary = "修改todo信息")
  public Response<?> putTodo(@Valid @RequestBody Todo todoInfo) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
    // 没有登陆
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Integer todoId = todoInfo.getTodoId();
    Optional<Todo> todo = todoRepository.findById(todoId);

//    if (todo.isEmpty()) {
//      return Response.notFound("没有找到todo哦！");
//    }
    BeanUtils.copyProperties(todoInfo, todo.get());
    return Response.ok(todoRepository.save(todo.get()));
  }


  @PutMapping("/check/{todoId}")
  @Operation(summary = "完成Todo")
  public Response<?> checkTodo(@PathVariable Integer todoId) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
    // 没有登陆
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Optional<Todo> todo = todoRepository.findById(todoId);

//    if (todo.isEmpty()) {
//      return Response.notFound("没有找到todo哦！");
//    }
    todo.get().setTodoCheck(true);
    return Response.ok(todoRepository.save(todo.get()));
  }

  @GetMapping("/get/{todoId}")
  @Operation(summary = "查看todo详情")
  public Response<?> getTodo(@PathVariable Integer todoId) {
    Optional<Todo> todo = todoRepository.findById(todoId);
//  UserSelfVO userSelfVO = new UserSelfVO();
//  BeanUtils.copyProperties(user.get(), userSelfVO);
    return Response.ok(todo);
  }

}
