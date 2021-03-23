package cpt202.groupwork.dto;

import java.util.Date;
import lombok.Data;

@Data
public class TodoCheckDTO {

  //完成todo时需要修改的属性

  //唯一标识一个todo
  private Integer todoId;

  //todo是否完成
  private Boolean todoCheck;

}
