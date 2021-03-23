package cpt202.groupwork.entity.relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class ProjectMember {

  private static final long serialVersionUID = 1L;
  /**
   * 主键，唯一标识一个
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer projectMemberId;

  /**
   * 项目id
   */
  @Column(name = "project_id")
  private Integer projectId;

  /**
   * 用户id user_id
   */
  @Column(name = "member_id")
  private Integer memberId;

}
