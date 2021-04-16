package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class MissionDTO {
  //添加一个新的mission需要用到的属性


  // mission所属的gantt
  private Long missionId;
  // mission的名字
  @Size(min = 1, max = 20, message = "mission名称必须在1到20个字之间哦")
  private String missionName;

  // mission所属的gantt
  private Integer ganttId;

  // mission开始时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionStart;

  // mission持续时间
  private Integer missionDuration;

  // mission的进度
  private Float missionProgress;


}
