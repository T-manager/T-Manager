package cpt202.groupwork.service;

import cpt202.groupwork.dto.ProjectDetailDTO;
import org.json.JSONException;

import java.util.List;

public interface ProjectService {
    List<ProjectDetailDTO> searchUserProject(String username, String json) throws JSONException;
}