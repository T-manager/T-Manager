package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Project;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

  /**
   * 通过 todolistId 找到 List<Todo>,按照ddl降序排列
   *
   * @param todolistId
   * @return List<TodoList>
   */
  List<Todo> findByTodolistIdOrderByTodoDdlAsc(Integer todolistId);

}
