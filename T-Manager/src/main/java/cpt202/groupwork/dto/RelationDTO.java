package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelationDTO {
    //the attribute need when create a relation

    //user id of the relation
    @NotNull(message = "userId cannot be null")
    private String memberName;

    //project id of the relation
    @NotNull(message = "projectId cannot be null")
    private int projectId;
}
