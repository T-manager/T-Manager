package cpt202.groupwork.service;

import cpt202.groupwork.dto.MemberDTO;
import cpt202.groupwork.dto.ProjectDetailDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.User;

import java.util.List;

public interface RelationService {
    List<ProjectDetailDTO> getUserProject(String username);
    List<MemberDTO> getProjectUser(Integer projectId);
}
