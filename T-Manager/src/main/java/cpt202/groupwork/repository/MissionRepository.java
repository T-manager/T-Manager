package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Mission;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Project;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

  /**
   * Using projectId, find List<Mission> in mission database
   *
   * @param GanttId
   * @return List<Gantt>
   */
  List<Mission> findByGanttId(Integer GanttId);

}