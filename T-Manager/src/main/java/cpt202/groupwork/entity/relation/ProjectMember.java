package cpt202.groupwork.entity.relation;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProjectMember implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Primary Key, unique identification of the relation
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer projectMemberId;

  /**
   * ID of the project
   */
  @Column(name = "project_id")
  private Integer projectId;

  /**
   * name of the project member
   */
  @Column(length = 20, name = "member_name")
  private String memberName;

}
