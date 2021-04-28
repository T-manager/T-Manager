package cpt202.groupwork.service.impl;

import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.TodolistService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: TodolistServiceImpl
 * @description: implements services of todolist
 * @Author: CPT202 Group 2
 * @version 1.0
 */

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

  /**
   * find todolist and following todos by project id in todolist database
   * @param projectId
   * @return List<TodolistViewDTO>
   */
  @Override
  public List<TodolistViewDTO> getTodolist(Integer projectId) {
    List<TodolistViewDTO> todolistViewDTOs = new ArrayList<>();
    List<Todolist> todolists = todoListRepository.findByProjectId(projectId);

    for (Todolist todoList : todolists) {
      TodolistViewDTO todolistViewDTO = new TodolistViewDTO();
      BeanUtils.copyProperties(todoList, todolistViewDTO);
      todolistViewDTO.setProjectName(projectRepository.findByProjectId(projectId).get().getProjectName());
      todolistViewDTOs.add(todolistViewDTO);

      List<TodoViewDTO> todoViewDTOs = new ArrayList<>();
      List<Todo> todos = todoRepository.findByTodolistId(todoList.getTodolistId());

      for (Todo todo : todos) {
        TodoViewDTO todoViewDTO = new TodoViewDTO();
        BeanUtils.copyProperties(todo, todoViewDTO);
        todoViewDTOs.add(todoViewDTO);
      }

      todolistViewDTO.setTodoViewDTO(todoViewDTOs);
    }

    return todolistViewDTOs;

  }
}
