package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MissionViewDTO {

  //在Gantt查看mission显示的属性

  //唯一标识一个mission
  private Integer missionId;

  //上级 mission ID
  private Integer ganttId;

  //mission的名字
  private String missionName;

  //mission的详情
  private String missionDetail;

  //mission的开始时间
  private Date missionStart;

  //mission的结束时间
  private Date missionEnd;

  //todo是否完成
  private Boolean missionCheck;

  //mission的执行人
  private Integer missionMember;

}
