package cpt202.groupwork.dto;

import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @className: GranttDTO
 * @description: The attribute needed when create a Gantt
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
public class GanttDTO {

  // gantt name
  @Size(min = 1, max = 20, message = "gantt name must be within 20 words")
  private String ganttName;

  // project id that gantt belong to
  private Integer projectId;


}
