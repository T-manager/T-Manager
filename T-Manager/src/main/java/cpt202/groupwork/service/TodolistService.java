package cpt202.groupwork.service;

import cpt202.groupwork.dto.TodolistViewDTO;
import java.util.List;

public interface TodolistService {

  List<TodolistViewDTO> getTodolist(Integer projectId);

  List<TodolistViewDTO> searchTodos(Integer projectId, String todoName);
}
