package cpt202.groupwork.service.impl;

import cpt202.groupwork.dto.MemberDTO;
import cpt202.groupwork.dto.ProjectDetailDTO;
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
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

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
    public List<ProjectDetailDTO> getUserProject(String username){
        List<ProjectDetailDTO> userProject = new ArrayList<>();
        Optional<User> user = userRepository.findByUserName(username);
        List<ProjectMember> projectMembers = relationRepository.findByMemberId(user.get().getUserId());
        for (ProjectMember projectMember : projectMembers){
            Optional<Project> project = projectRepository.findByProjectId(projectMember.getProjectId());
            ProjectDetailDTO pdDTO = new ProjectDetailDTO();
            BeanUtils.copyProperties(project.get(), pdDTO);
            Optional<User> po = userRepository.findById(project.get().getProjectOwnerId());
            pdDTO.setProjectOwner(po.get().getUserName());
            userProject.add(pdDTO);
        }
        return userProject;
    }

    /**
     * find all user information under a project
     *
     * @param projectId
     * @return List<User>
     */
    public List<MemberDTO> getProjectUser(Integer projectId){
        List<ProjectMember> projectMembers = relationRepository.findByProjectId(projectId);
        List<MemberDTO> memberDTOs = new ArrayList<>();
        for (ProjectMember projectMember : projectMembers){
            MemberDTO memberDTO = new MemberDTO();
            BeanUtils.copyProperties(projectMember, memberDTO);
            Optional<User> user = userRepository.findById(projectMember.getMemberId());
            memberDTO.setMemberName(user.get().getUserName());
            memberDTO.setMemberAvatar(user.get().getUserAvatar());
            memberDTOs.add(memberDTO);
        }
        return memberDTOs;
    }
}
