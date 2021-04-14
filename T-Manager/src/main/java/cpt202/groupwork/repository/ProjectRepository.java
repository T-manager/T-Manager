package cpt202.groupwork.repository;

import cpt202.groupwork.entity.Project;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    /**
     * find the project by projectId
     *
     * @param projectId
     * @return Optional<Project>
     */
    Optional<Project> findByProjectId(Integer projectId);
}
