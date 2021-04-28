package cpt202.groupwork.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * @className: MemberDTO
 * @description: data needed when viewing a member
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Data
public class MemberDTO {

    private Integer projectMemberId;

    private Integer projectId;

    private Integer memberId;

    private String memberName;

    private String memberAvatar;

    private String memberRole;
}

