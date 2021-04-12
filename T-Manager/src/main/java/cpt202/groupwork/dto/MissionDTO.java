package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class MissionDTO {
  //添加一个新的mission需要用到的属性

  // mission的名字
  @Size(min = 1, max = 20, message = "mission名称必须在1到20个字之间哦")
  private String missionName;

  // mission所属的gantt
  private Integer ganttId;

  // mission的详细介绍
  @Size(min = 0, max = 100, message = "详细介绍必须小于100个字哦")
  private String missionDetail;

  // mission开始时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionStart;

  // mission结束时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionEnd;

  // mission的执行人
  private Integer missionMember;

}
