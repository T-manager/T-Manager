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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Mission implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键，唯一标识mission
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer missionId;

  /**
   * gantt的id
   */
  @Column(name = "gantt_id")
  private Integer ganttId;


  /**
   * mission的名字
   */
  @Column(length = 20, name = "mission_name")
  private String missionName;

  /**
   * mission的开始时间
   */
  @Column(name = "mission_start")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionStart;

  /**
   * mission的结束时间
   */
  @Column(name = "mission_end")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionEnd;

  /**
   * mission的行数
   */
  @Column(name = "mission_line")
  private Integer missionLine;

  /**
   * mission的描述
   */
  @Column(length = 100, name = "mission_detail")
  private String missionDetail;

  /**
   * mission的执行人
   */
  @Column(name = "mission_member")
  private String missionMember;

  /**
   * mission的是否完成
   */
  @Column(name = "mission_check")
  private Boolean missionCheck;
}
