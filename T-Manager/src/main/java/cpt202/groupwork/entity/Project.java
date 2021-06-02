package cpt202.groupwork.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Project implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Primary Key, unique identification of the project
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer projectId;

  /**
   * name of the project
   */
  @NonNull
  @Column(length = 20, name = "project_name")
  private String projectName;

  /**
   * description of the project
   */
  @Column(length = 100, name = "project_detail")
  private String projectDetail;

  /**
   * name of the project owner
   */
  @NonNull
  @Column(name = "project_owner_id")
  private Integer projectOwnerId;

  /**
   * name of the project owner
   */
  @NonNull
  @Column(length = 100, name = "project_type")
  private String projectType;
}
