package cpt202.groupwork.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Data
public class MemberDTO {

    private Integer projectMemberId;

    private Integer projectId;

    private Integer memberId;

    private String memberName;

    private String memberAvatar;

    private String memberRole;
}

