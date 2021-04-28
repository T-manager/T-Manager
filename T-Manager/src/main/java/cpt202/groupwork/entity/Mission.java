package cpt202.groupwork.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: Mission
 * @description: connect to the SQL database for Mission entity
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mission implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * primary key, to identify the mission
   */
  @Id
  @Column(name = "mission_id")
  private Long missionId;

  /**
   *  id of gantt which belongs to
   */
  @Column(name = "gantt_id")
  private Integer ganttId;


  /**
   * mission name
   */
  @Column(length = 20, name = "mission_name")
  private String missionName;

  /**
   * mission start time
   */
  @Column(name = "mission_start")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionStart;

  /**
   * mission duration time
   */
  @Column(name = "mission_duration")
  private Integer missionDuration;

  /**
   * mission progress
   */
  @Column(name = "mission_progress")
  private Float missionProgress;


  @Column(name = "mission_parent")
  private Long missionParent;

}
