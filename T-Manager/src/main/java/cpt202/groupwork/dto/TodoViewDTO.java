package cpt202.groupwork.dto;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TodoViewDTO {

  // 在todolist查看todo显示的属性

  // 唯一标识一个todo
  private Integer todoId;

  // 上级 todolist ID
  private Integer todolistId;

  // todo的名字
  @Size(min = 1, max = 20, message = "todo名称必须在1到20个字之间哦")
  private String todoName;

  // todo的详情
  @Size(min = 0, max = 100, message = "详细介绍必须小于100个字哦")
  private String todoDetail;

  // todo的截止日期
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date todoDdl;

  // todo是否完成
  private Boolean todoCheck;

  // todo的执行人
  private String todoMember;

  // todo 执行人的头像
  private String todoMemberAvatar;

}
