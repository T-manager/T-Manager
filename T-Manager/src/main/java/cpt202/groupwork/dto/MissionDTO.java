package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @className: MissionDTO
 * @description: Attributed used when created a mission
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Data
public class MissionDTO {

  private Long missionId;

  @Size(min = 1, max = 20, message = "The mission name must be within 20 words")
  private String missionName;

  // gantt id which mission belong to
  private Integer ganttId;

  // start time of the mission
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date missionStart;

  // duration time of the mission
  private Integer missionDuration;

  // progress of the mission
  private Float missionProgress;

  //key used to identify mission
  private Long missionParent;
}
