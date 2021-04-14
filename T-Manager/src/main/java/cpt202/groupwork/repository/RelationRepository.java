package cpt202.groupwork.repository;

import cpt202.groupwork.entity.relation.ProjectMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<ProjectMember, Integer>{
    /**
     * get the list of relation which contain certain member by memberName
     *
     * @param memberName
     * @return List<ProjectMember>
     */
    List<ProjectMember> findByMemberName(String memberName);

    /**
     * get the list of relation which contain certain project by projectId
     *
     * @param projectId
     * @return List<ProjectMember>
     */
    List<ProjectMember> findByProjectId(Integer projectId);
}
