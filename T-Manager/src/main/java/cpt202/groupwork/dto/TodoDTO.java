package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TodoDTO {
  //添加一个新的todo需要用到的属性

  // todo的名字
  @Size(min = 1, max = 20, message = "todo名称必须在1到20个字之间哦")
  private String todoName;

  // todo所属的todolist
  private Integer todolistId;

  // todo的详细介绍
  @Size(min = 0, max = 100, message = "详细介绍必须小于100个字哦")
  private String todoDetail;

  // todo截止日期
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date todoDdl;

  // todo的执行人
  private String todoMember;

}
