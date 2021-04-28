package cpt202.groupwork.dto;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * @className: GanttViewDTO
 * @description: data needed when view a gantt
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Data
public class GanttViewDTO {

  // primary key ganttId
  private Integer ganttId;

  // corresponding projectId
  private Integer projectId;

  //todolist name
  private String ganttName;

  // total number of the mission
  private Integer ganttTotalNum;

  //mission of the gantt
  private List<MissionViewDTO> missionViewDTO;

}
