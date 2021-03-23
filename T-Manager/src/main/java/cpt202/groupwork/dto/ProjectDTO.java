package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDTO {
  // 新建project需要的属性

  // project的名字
  @Size(min = 1, max = 20, message = "todo名称必须在1到20个字之间哦")
  private String projectName;

  // project的详细介绍
  @Size(min = 0, max = 100, message = "详细介绍必须小于100个字哦")
  private String projectDetail;


  // project的所属人
  private Integer projectOwner;

  //project的种类
  private Boolean projectType;

}
