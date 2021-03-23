package cpt202.groupwork.service.impl;

import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.TodoListRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.TodolistService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodolistServiceImpl  implements TodolistService {
  @Autowired
  TodoListRepository todoListRepository;

  @Autowired
  TodoRepository todoRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public List<TodolistViewDTO> getTodolist(Integer projectId) {
    List<TodolistViewDTO> todolistViewDTOs= new ArrayList<>();
    List<Todolist> todolists = todoListRepository.findByProjectId(projectId);

    for (Todolist todoList : todolists) {
      TodolistViewDTO todolistViewDTO = new TodolistViewDTO();
      BeanUtils.copyProperties(todoList, todolistViewDTO);
//      todolistViewDTO.setAvatar(userRepository.findAvatarByUsername(todolistViewDTO.getUsername()));
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
