package cpt202.groupwork.dto;

import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @className: TodolistDTO
 * @description: attributes used when create a todolist
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
public class TodolistDTO {

  // todolist name
  @Size(min = 1, max = 20, message = "the name of the todolist must between 1 and 20 words")
  private String todolistName;

  //project that todolist belongs to
  private Integer projectId;


}
