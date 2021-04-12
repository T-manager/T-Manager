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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gantt implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键，唯一标识Gantt
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer ganttId;

  /**
   * 所属project的id
   */
  @Column(name = "project_id")
  private Integer projectId;

  /**
   * Gantt的name
   */
  @Column(length = 20, name = "gantt_name")
  private String ganttName;

  /**
   * Gantt的detail
   */
  @Column(length = 100, name="gantt_detail")
  private String ganttDetail;

  /**
   * 总共有多少的mission
   */
  @Column(name = "gantt_total_num")
  private Integer ganttTotalNum = 0;


}
