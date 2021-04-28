package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

/**
 * @className: TodoViewDTO
 * @description: Attributes used when viewing todo
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Data
public class TodoViewDTO {

  private Integer todoId;

  private Integer todolistId;

  private String todoName;

  private String todoDetail;

  private Date todoDdl;

  private Boolean todoCheck;

}
