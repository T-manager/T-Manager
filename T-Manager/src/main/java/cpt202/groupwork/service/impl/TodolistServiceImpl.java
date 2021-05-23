package cpt202.groupwork.service.impl;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.TodolistService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodolistServiceImpl implements TodolistService {

  @Autowired
  TodolistRepository todoListRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public Response<?> getTodolist(Integer projectId) {
    List<TodolistViewDTO> todolistViewDTOs = new ArrayList<>();
    Optional<Project> project = projectRepository.findById(projectId);
    if (project.equals(Optional.empty())) {
      return Response.exceptionHandling(30, "project not exist");
    }
    List<Todolist> todolists = todoListRepository.findByProjectIdOrderByTodolistIdAsc(projectId);
    for (Todolist todoList : todolists) {
      TodolistViewDTO todolistViewDTO = new TodolistViewDTO();
      BeanUtils.copyProperties(todoList, todolistViewDTO);
      todolistViewDTO.setProjectName(projectRepository.findByProjectId(projectId).get().getProjectName());
      // todolistViewDTO.setAvatar(userRepository.findAvatarByUsername(todolistViewDTO.getUsername()));
      todolistViewDTOs.add(todolistViewDTO);
      List<TodoViewDTO> todoViewDTOs = new ArrayList<>();
      List<Todo> todos = todoRepository.findByTodolistIdOrderByTodoDdlAsc(todoList.getTodolistId());

      for (Todo todo : todos) {
        TodoViewDTO todoViewDTO = new TodoViewDTO();
        BeanUtils.copyProperties(todo, todoViewDTO);
        todoViewDTO.setTodoMember(userRepository.findByUserId(todo.getTodoMember()).get().getUserName());
        todoViewDTO.setTodoMemberAvatar(userRepository.findByUserId(todo.getTodoMember()).get().getUserAvatar());
        todoViewDTOs.add(todoViewDTO);
      }
      todolistViewDTO.setTodoViewDTO(todoViewDTOs);
    }
    return Response.ok(todolistViewDTOs);

  }

  @Override
  public List<TodolistViewDTO> searchTodos(Integer projectId, String todoName) {
    List<TodolistViewDTO> todolistViewDTOs = new ArrayList<>();
    List<Todolist> todolists = todoListRepository.findByProjectIdOrderByTodolistIdAsc(projectId);
    for (Todolist todoList : todolists) {
      TodolistViewDTO todolistViewDTO = new TodolistViewDTO();
      BeanUtils.copyProperties(todoList, todolistViewDTO);
      todolistViewDTO.setProjectName(projectRepository.findByProjectId(projectId).get().getProjectName());
      todolistViewDTOs.add(todolistViewDTO);
      List<TodoViewDTO> todoViewDTOs = new ArrayList<>();
      List<Todo> todos = todoRepository.findByTodolistIdAndTodoName(todoList.getTodolistId(), todoName);
      List<Todo> todoLike = todoRepository.findByTodolistIdAndTodoNameLike(todoList.getTodolistId(),
          "%" + todoName + "%");
      for (Todo todol : todoLike) {
        if (todos.contains(todol))
          continue;
        else
          todos.add(todol);
      }
      for (Todo todo : todos) {
        TodoViewDTO todoViewDTO = new TodoViewDTO();
        BeanUtils.copyProperties(todo, todoViewDTO);
        todoViewDTO.setTodoMember(userRepository.findByUserId(todo.getTodoMember()).get().getUserName());
        todoViewDTO.setTodoMemberAvatar(userRepository.findByUserId(todo.getTodoMember()).get().getUserAvatar());
        todoViewDTOs.add(todoViewDTO);
      }
      todolistViewDTO.setTodoViewDTO(todoViewDTOs);
    }

    return todolistViewDTOs;

  }
}
