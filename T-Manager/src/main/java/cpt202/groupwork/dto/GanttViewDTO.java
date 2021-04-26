package cpt202.groupwork.dto;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
public class GanttViewDTO {

  // 唯一标识todolist
  private Integer ganttId;

  //对应项目id
  private Integer projectId;

  //todolist的名字
  private String ganttName;

  //包含todo的总数
  private Integer ganttTotalNum;

  //包含的todo对象
  private List<MissionViewDTO> missionViewDTO;

}
