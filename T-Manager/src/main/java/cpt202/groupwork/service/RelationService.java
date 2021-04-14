package cpt202.groupwork.service;

import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.User;

import java.util.List;

public interface RelationService {
    List<Project> getUserProject(String username);
    List<User> getProjectUser(Integer projectId);
}