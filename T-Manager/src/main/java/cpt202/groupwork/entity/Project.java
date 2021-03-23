package cpt202.groupwork.entity;

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

public class Project implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键，唯一标识project
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer projectId;

  /**
   * project的名字
   */
  @Column(length = 20, name = "project_name")
  private String projectName;

  /**
   * project的描述
   */
  @Column(length = 100, name = "project_detail")
  private String projectDetail;

  /**
   * project的拥有者user_id
   */
  @Column(name = "project_owner")
  private Integer projectOwner;

  /**
   * project的种类 个人/团体
   */
  @Column(name = "project_type")
  private Boolean projectType;


}
