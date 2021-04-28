package cpt202.groupwork.dto;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * @className: TodolistViewDTO
 * @description: Attributes needed when viewing a todolist
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Data
public class TodolistViewDTO {

  // identify the todolist
  private Integer todolistId;

  // corresponding project id
  private Integer projectId;

  // corresponding project name
  private String projectName;

  //todolist name
  private String todolistName;

  // todolist number
  private Integer todolistTotalNum;

  // complete number of the todolist
  private Integer todolistCompleteNum;

  // contained todoview object
  private List<TodoViewDTO> todoViewDTO;

}
