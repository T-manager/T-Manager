package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDTO {
  // all attributes needed when create a new project

  // name of the project
  @Size(min = 1, max = 20, message = "the length of the project cannot exceed 20")
  @NotNull(message = "The name of the project cannot be null")
  private String projectName;

  // detailed specification of the project
  @Size(min = 0, max = 100, message = "the detail of the project cannot exceed 100")
  private String projectDetail;


  // owner of the project
  private String projectOwner;

}
