package cpt202.groupwork.repository;


import cpt202.groupwork.entity.Mission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
//  List<TodoRepository> findByUsername(String username);
  /**
   * 通过 projectId 找到 List<Mission>
   *
   * @param GanttId
   * @return List<Gantt>
   */
  List<Mission> findByGanttId(Integer GanttId);

}