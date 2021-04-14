package cpt202.groupwork.dto;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
public class TodolistViewDTO {

  // 唯一标识todolist
  private Integer todolistId;

  //对应项目id
  private Integer projectId;

  //todolist的名字
  private String todolistName;

  //包含todo的总数
  private Integer todolistTotalNum;

  // 完成的todo的数量
  private Integer todolistCompleteNum;

  //包含的todo对象
  private List<TodoViewDTO> todoViewDTO;

}
