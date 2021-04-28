package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @className: RelationDTO
 * @description: the attribute need when create a relation
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
public class RelationDTO {
    //user id of the relation
    @NotNull(message = "userId cannot be null")
    private String memberName;

    //project id of the relation
    @NotNull(message = "projectId cannot be null")
    private int projectId;
}
