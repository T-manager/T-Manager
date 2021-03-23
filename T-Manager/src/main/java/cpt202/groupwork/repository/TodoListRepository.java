package cpt202.groupwork.repository;

import cpt202.groupwork.entity.TodoList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Integer> {

//  List<TodoList> findByUsername(String username);

  /**
   * 通过 projectId 找到 List<TodoList>
   *
   * @param projectId
   * @return List<TodoList>
   */
  List<TodoList> findByProjectId(Integer projectId);

}
