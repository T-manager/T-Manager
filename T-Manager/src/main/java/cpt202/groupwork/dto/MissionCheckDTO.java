package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MissionCheckDTO {
  //完成mission时需要修改的属性

  //唯一标识一个mission
  private Integer missionId;

  //mission是否完成
  private Boolean missionCheck;

}
