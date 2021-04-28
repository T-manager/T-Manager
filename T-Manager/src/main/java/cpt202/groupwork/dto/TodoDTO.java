package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @className: TodoDTO
 * @description: Attributes needed when created a new todo
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
public class TodoDTO {

  // to-do name
  @Size(min = 1, max = 20, message = "the name of the todo must between 1 and 20 words")
  private String todoName;

  // todolist that to-do belong to
  private Integer todolistId;

  // detail description about to-do
  @Size(min = 0, max = 100, message = "the detail of the todo must within 100 words")
  private String todoDetail;

  // deadline of the to-do
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date todoDdl;

  //  executor of the to-do
  private Integer todoMember;

}
