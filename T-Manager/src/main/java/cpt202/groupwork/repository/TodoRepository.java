package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Project;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
//  List<TodoRepository> findByUsername(String username);
  /**
   * Using tofolistId, find List<Todo> in todo database
   *
   * @param todolistId
   * @return List<Todo>
   */
  List<Todo> findByTodolistId(Integer todolistId);

}
