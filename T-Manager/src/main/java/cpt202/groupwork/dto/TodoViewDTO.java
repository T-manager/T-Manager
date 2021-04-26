package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

@Data
public class TodoViewDTO {

  //在todolist查看todo显示的属性

  //唯一标识一个todo
  private Integer todoId;

  //上级 todolist ID
  private Integer todolistId;

  //todo的名字
  private String todoName;

  //todo的详情
  private String todoDetail;

  //todo的截止日期
  private Date todoDdl;

  //todo是否完成
  private Boolean todoCheck;

}
