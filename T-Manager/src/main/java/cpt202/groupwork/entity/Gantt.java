package cpt202.groupwork.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: Gantt
 * @description: connect to the SQL database for Gantt entity
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gantt implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * primary key, to identify the gantt
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer ganttId;

  /**
   * id of the project that gantt belong to
   */
  @Column(name = "project_id")
  private Integer projectId;

  /**
   * name of the gantt
   */
  @Column(length = 20, name = "gantt_name")
  private String ganttName;

  /**
   * detail of the gantt
   */
  @Column(length = 100, name="gantt_detail")
  private String ganttDetail;

  /**
   * total number of the mission that gantt have
   */
  @Column(name = "gantt_total_num")
  private Integer ganttTotalNum = 0;


}
