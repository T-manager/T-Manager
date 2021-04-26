package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Todolist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GanttRepository extends JpaRepository<Gantt, Integer> {

//  List<Gantt> findByUsername(String username);

  /**
   * 通过 projectId 找到 List<Gantt>
   *
   * @param projectId
   * @return List<Gantt>
   */
  List<Gantt> findByProjectId(Integer projectId);

}
