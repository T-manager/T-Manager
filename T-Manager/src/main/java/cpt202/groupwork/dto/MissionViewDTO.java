package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MissionViewDTO {

  //在Gantt查看mission显示的属性

  //唯一标识一个mission
  private Long missionId;

  //上级 mission ID
  private Integer ganttId;

  //mission的名字
  private String missionName;

  //mission的开始时间
  private Date missionStart;

  //mission的结束时间
  private Integer missionDuration;

  //mission 进度
  private Float missionProgress;

  //mission parent
  private Long missionParent;
}
