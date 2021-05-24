package cpt202.groupwork.service;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.TodolistViewDTO;
import java.util.List;

public interface TodolistService {

  Response<?> getTodolist(Integer projectId);

  List<TodolistViewDTO> searchTodos(Integer projectId, String todoName);
  Response<?> deleteTodolist(Integer todolistId);

}
