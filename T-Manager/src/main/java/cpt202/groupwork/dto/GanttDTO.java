package cpt202.groupwork.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class GanttDTO {
  //添加一个新的gantt需要用到的属性

  // gantt的名字
  @Size(min = 1, max = 20, message = "gantt名称必须在1到20个字之间哦")
  private String ganttName;

  // gantt所属的project
  private Integer projectId;


}
