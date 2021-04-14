package cpt202.groupwork.service.impl;

import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.entity.relation.ProjectMember;
import cpt202.groupwork.repository.*;
import cpt202.groupwork.service.RelationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationServiceImpl implements RelationService{
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RelationRepository relationRepository;

    /**
     * find all project information user owns
     *
     * @param username
     * @return List<Project>
     */
    public List<Project> getUserProject(String username){
        List<Project> userProject = new ArrayList<>();
        List<ProjectMember> projectMembers = relationRepository.findByMemberName(username);
        for (ProjectMember projectMember : projectMembers){
            userProject.add(projectRepository.findByProjectId(projectMember.getProjectId()).get());
        }
        return userProject;
    }

    /**
     * find all user information under a project
     *
     * @param projectId
     * @return List<User>
     */
    public List<User> getProjectUser(Integer projectId){
        List<User> projectUser = new ArrayList<>();
        List<ProjectMember> projectMembers = relationRepository.findByProjectId(projectId);
        for (ProjectMember projectMember : projectMembers){
            projectUser.add(userRepository.findByUsername(projectMember.getMemberName()).get());
        }
        return projectUser;
    }
}
