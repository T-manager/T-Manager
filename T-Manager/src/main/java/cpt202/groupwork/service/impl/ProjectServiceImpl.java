package cpt202.groupwork.service.impl;

import cpt202.groupwork.dto.ProjectDetailDTO;
import cpt202.groupwork.service.ProjectService;
import cpt202.groupwork.service.RelationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @className: ProjectServiceImpl
 * @Description: implement the service of project like search
 * @version: v1.８.0
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    RelationService relationService;

    public List<ProjectDetailDTO> searchUserProject(String username, String namePattern) {
        List<ProjectDetailDTO> pdDTO = relationService.getUserProject(username);
        List<ProjectDetailDTO> pdDTOMatch = new ArrayList<>();
        namePattern = namePattern.toLowerCase();
        for (ProjectDetailDTO tmp : pdDTO){
            if(tmp.getProjectName().toLowerCase().contains(namePattern)) {
                pdDTOMatch.add(tmp);
            }
        }
        return pdDTOMatch;
    }
}