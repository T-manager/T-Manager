package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Todolist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodolistRepository extends JpaRepository<Todolist, Integer> {

  // List<TodoList> findByUsername(String username);

  /**
   * 通过 projectId 找到 List<TodoList>
   *
   * @param projectId
   * @return List<TodoList>
   */
  List<Todolist> findByProjectId(Integer projectId);

  /**
   * 通过 projectId 找到 List<TodoList>
   *
   * @param projectId
   * @return List<TodoList>
   */
  List<Todolist> findByProjectIdOrderByTodolistIdAsc(Integer projectId);
}
