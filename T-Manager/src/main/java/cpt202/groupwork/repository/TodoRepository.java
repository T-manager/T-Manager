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

  /**
   * 通过 todomember 找到 List<Todo>,按照ddl降序排列
   *
   * @param todoMember
   * @return List<TodoList>
   */
  List<Todo> findByTodoMemberOrderByTodoDdlAsc(Integer todoMember);

  /**
   * 通过 todoName和todolistId 找到 List<Todo>
   *
   * @param todoName
   * @return List<TodoList>
   */
  List<Todo> findByTodolistIdAndTodoName(Integer todolistId, String todoName);

  /**
   * 通过 todolistId 和todoname模糊 搜索找到 List<Todo>
   *
   * @param todoName
   * @return List<TodoList>
   */
  List<Todo> findByTodolistIdAndTodoNameLike(Integer todolistId, String todoName);

}
