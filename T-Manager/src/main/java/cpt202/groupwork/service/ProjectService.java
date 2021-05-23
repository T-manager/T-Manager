package cpt202.groupwork.service;

import cpt202.groupwork.Response;
import cpt202.groupwork.dto.ProjectDetailDTO;
import org.json.JSONException;

import java.util.List;

public interface ProjectService {
    Response<?> searchUserProject(String username, String namePattern);
}