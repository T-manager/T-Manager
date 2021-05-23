package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodoCalendarDTO;
import cpt202.groupwork.dto.TodoDTO;
import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.TodolistService;
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

  @Autowired
  TodolistService todolistService;

  /**
   * Upload a todo
   *
   * @param todoDTO, the name of user who is current uploding the file
   * @return filename, including filetype
   */
  @PostMapping("/add")
  @Operation(summary = "Add todo with todoListId and todoCreateDTO")
  public Response<?> createTodo(@Valid @RequestBody TodoDTO todoDTO) {

    // check if todo exist
    Integer todolistId = todoDTO.getTodolistId();
    Optional<Todolist> todolist = todolistRepository.findById(todolistId);
    if (todolist.equals(Optional.empty())) {
      return Response.exceptionHandling(302, "todolist does not exist");
    }

    // check the todoName meet requirements
    int nameLength = todoDTO.getTodoName().length();
    if (nameLength < 1 || nameLength > 20) {
      return Response.exceptionHandling(341, "The TODO name should be between 1 and 20 characters");
    }

    // check the todoDetail meet requirements
    int detailLength = todoDTO.getTodoDetail().length();
    if (detailLength < 1 || detailLength > 100) {
      return Response.exceptionHandling(343, "The detail should be less than 100 characters");
    }
    // check if user exist
    Optional<User> user = userRepository.findByUserName(todoDTO.getTodoMember());
    if (user.equals(Optional.empty())) {
      return Response.exceptionHandling(305, "user does not exist");
    }
    Todo todo = new Todo();
    BeanUtils.copyProperties(todoDTO, todo);
    todo.setTodoMember(user.get().getUserId());
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

    Optional<Todo> todo = todoRepository.findById(todoId);
    if (todo.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "todo does not exist");
    }
    Todolist todolist = todolistRepository.findById(todo.get().getTodolistId()).get();
    todolist.setTodolistTotalNum(todolist.getTodolistTotalNum() - 1);
    if (todo.get().getTodoCheck() == true) {
      todolist.setTodolistCompleteNum(todolist.getTodolistCompleteNum() - 1);
    }
    todolistRepository.save(todolist);
    todoRepository.deleteById(todoId);
    return Response.ok();
  }

  @PutMapping("/edit")
  @Operation(summary = "修改todo信息")
  public Response<?> modifyTodo(@Valid @RequestBody TodoViewDTO todoInfo) {

    Integer todoId = todoInfo.getTodoId();
    Optional<Todo> todo = todoRepository.findById(todoId);
    if (todo.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "todo does not exist");
    }

    // check the todoName meet requirements
    int nameLength = todoInfo.getTodoName().length();
    if (nameLength < 1 || nameLength > 20) {
      return Response.exceptionHandling(341, "The TODO name should be between 1 and 20 characters");
    }

    // check the todoDetail meet requirements
    int detailLength = todoInfo.getTodoDetail().length();
    if (detailLength < 1 || detailLength > 100) {
      return Response.exceptionHandling(343, "The detail should be less than 100 characters");
    }
    Optional<User> user = userRepository.findByUserName(todoInfo.getTodoMember());
    if (user.equals(Optional.empty())) {
      return Response.exceptionHandling(307, "user does not exist");
    }
    BeanUtils.copyProperties(todoInfo, todo.get());
    todo.get().setTodoMember(user.get().getUserId());
    todoRepository.save(todo.get());
    return Response.ok();
  }

  @PutMapping("/check/{todoId}")
  @Operation(summary = "完成Todo")
  public Response<?> checkTodo(@PathVariable Integer todoId) {
    Optional<Todo> todo = todoRepository.findById(todoId);
    if (todo.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "todo does not exist");
    }

    Optional<Todolist> todolist = todolistRepository.findById(todo.get().getTodolistId());
    if (todolist.equals(Optional.empty())) {
      return Response.exceptionHandling(30, "todolist does not exist");
    }
    // 如果是没有完成的todo
    if (todo.get().getTodoCheck() == false) {
      todolist.get().setTodolistCompleteNum(todolist.get().getTodolistCompleteNum() + 1);
    } else {
      todolist.get().setTodolistCompleteNum(todolist.get().getTodolistCompleteNum() - 1);
    }
    todo.get().setTodoCheck(!todo.get().getTodoCheck());
    todoRepository.save(todo.get());
    return Response.ok();
  }

  @GetMapping("/get/{todoId}")
  @Operation(summary = "查看todo详情")
  public Response<?> getTodo(@PathVariable Integer todoId) {
    Optional<Todo> todo = todoRepository.findById(todoId);

    if (todo.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "todo does not exist");
    }
    // UserSelfVO userSelfVO = new UserSelfVO();
    // BeanUtils.copyProperties(user.get(), userSelfVO);
    return Response.ok(todo);
  }

  @GetMapping("/get/member/{todoMember}")
  @Operation(summary = "查看某执行人的所有todo")
  public Response<?> getTodoByUsername(@PathVariable String todoMember) {
    Optional<User> user = userRepository.findByUserName(todoMember);
    if (user.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "executor does not exist");
    }
    List<Todo> todos = todoRepository.findByTodoMemberOrderByTodoDdlAsc(user.get().getUserId());
    List<TodoCalendarDTO> todoCalendarDTOs = new ArrayList<>();
    for (Todo todo : todos) {
      TodoCalendarDTO todoCalendarDTO = new TodoCalendarDTO();
      BeanUtils.copyProperties(todo, todoCalendarDTO);
      // Get todolistName
      Optional<Todolist> todolist = todolistRepository.findById(todo.getTodolistId());
      todoCalendarDTO.setTodolistName(todolist.get().getTodolistName());
      // Get projectname
      todoCalendarDTO.setProjectId(todolist.get().getProjectId());
      Optional<Project> project = projectRepository.findById(todolist.get().getProjectId());
      todoCalendarDTO.setProjectName(project.get().getProjectName());
      todoCalendarDTOs.add(todoCalendarDTO);
    }
    return Response.ok(todoCalendarDTOs);
  }

  @GetMapping("/get/{projectId}/search/{todoName}")
  @Operation(summary = "在Dashboard搜索")
  public Response<?> searchTodo(@PathVariable Integer projectId, @PathVariable String todoName) {
    List<TodolistViewDTO> todolistViewDTOs = new ArrayList<>();
    todolistViewDTOs = todolistService.searchTodos(projectId, todoName);
    return Response.ok(todolistViewDTOs);
  }

}
