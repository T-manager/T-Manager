package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

/**
 * @className: TodoCheckDTO
 * @description: attributes needed to be modified when check to-do
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
public class TodoCheckDTO {

  //primary key used to identify to-do
  private Integer todoId;

  //whether the to-do is checked
  private Boolean todoCheck;

}
