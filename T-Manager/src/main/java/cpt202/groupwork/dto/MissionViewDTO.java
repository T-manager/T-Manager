package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

/**
 * @className: MissionViewDTO
 * @description: Attributes needed when viewing a mission
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Data
public class MissionViewDTO {

  private Long missionId;

  private Integer ganttId;

  private String missionName;

  private Date missionStart;

  private Integer missionDuration;

  private Float missionProgress;

  private Long missionParent;

}
