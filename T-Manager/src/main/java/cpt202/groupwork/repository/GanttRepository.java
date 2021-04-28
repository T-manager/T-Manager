package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Todolist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GanttRepository extends JpaRepository<Gantt, Integer> {


  /**
   * find List<Gantt> by projectId in gantt database
   *
   * @param projectId
   * @return List<Gantt>
   */
  List<Gantt> findByProjectId(Integer projectId);

}
