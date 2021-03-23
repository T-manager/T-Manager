package cpt202.groupwork.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class TodolistDTO {
  //添加一个新的todolist需要用到的属性

  // todolist的名字
  @Size(min = 1, max = 20, message = "todolist名称必须在1到20个字之间哦")
  private String todolistName;

  //  // todolist所属的project
  private Integer projectId;


}
