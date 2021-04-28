package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Todolist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodolistRepository extends JpaRepository<Todolist, Integer> {

  /**
   * Using projectId, find List<TodoList> in todolist database
   *
   * @param projectId
   * @return List<TodoList>
   */
  List<Todolist> findByProjectId(Integer projectId);

}
