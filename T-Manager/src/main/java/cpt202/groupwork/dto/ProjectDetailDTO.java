package cpt202.groupwork.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProjectDetailDTO {
    // all attributes needed when create a new project
    private Integer projectId;

    private Integer projectMemberId;

    // name of the project
    private String projectName;

    // detailed specification of the project
    @Size(min = 0, max = 100, message = "the detail of the project cannot exceed 100")
    private String projectDetail;

    // owner of the project
    private String projectOwner;

    // owner of the project
    private String projectType;
}