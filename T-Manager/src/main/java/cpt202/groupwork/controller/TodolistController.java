package cpt202.groupwork.controller;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodolistDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodoListRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
//import cpt202.groupwork.security.SecurityUtils;
import cpt202.groupwork.service.TodolistService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
@RequestMapping("/todolist")
public class TodolistController {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TodoListRepository todoListRepository;

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TodolistService todolistService;

  @PostMapping("/add")
  @Operation(summary = "通过 projectId 和 todolistCreateDTO 添加 todoList")
  public Response<?> postTodolist(@Valid @RequestBody TodolistDTO todolistDTO) {
//    如果用户没有登陆，不能添加新的todolist
//
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
//    如果项目不存在
//    if (!projectRepository.existsById(projectId)) {
//      return Response.bad("不存在此项目");
//    }

    Todolist todolist = new Todolist();
//    Discussion discussion = new Discussion();
    BeanUtils.copyProperties(todolistDTO, todolist);
    todoListRepository.save(todolist);
    return Response.ok(todolist);
  }

  @DeleteMapping("/delete/{todolistId}")
  @Operation(summary = "删除讨论")
  public Response<?> deleteDiscussion(@PathVariable Integer todolistId) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }

    Optional<Todolist> todolist = todoListRepository.findById(todolistId);
//    if (todoList.isEmpty()) {
//      return Response.notFound();
//    }

//    if (!username.get().equals(todolist.get().getUsername())) {
//      return Response.permissionDenied("只有自己才能删除哦！");
//    }
    todoListRepository.deleteById(todolistId);
    return Response.ok(todolist.get());
  }

  @PutMapping("/modify")
  @Operation(summary = "修改todolist信息")
  public Response<?> putProject(@Valid @RequestBody TodoList todolist) {
//    Optional<String> username = SecurityUtils.getCurrentUsername();
    // 没有登陆
//    if (username.isEmpty()) {
//      return Response.unAuth();
//    }
    Optional<TodoList> todoList = todoListRepository.findById(todolist.getTodolistId());

//    if (todo.isEmpty()) {
//      return Response.notFound("没有找到todo哦！");
//    }
    BeanUtils.copyProperties(todolist, todoList.get());
    return Response.ok(todoListRepository.save(todoList.get()));
  }


  @GetMapping("/get/{projectId}")
  @Operation(summary = "通过 projectid 查看所有的 todolist, 包括所属的todo")
  public Response<?> getDiscussion(@PathVariable Integer projectId) {
    List<TodolistViewDTO> todolistViewDTOs = new ArrayList<>();
//    Optional<String> username = SecurityUtils.getCurrentUsername();
    todolistViewDTOs = todolistService.getTodolist(projectId);
    return Response.ok(todolistViewDTOs);
  }
}
