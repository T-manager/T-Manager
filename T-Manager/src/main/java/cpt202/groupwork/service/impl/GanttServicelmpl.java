package cpt202.groupwork.service.impl;
//GanttServicelmpl
import cpt202.groupwork.Response;
import cpt202.groupwork.dto.GanttViewDTO;
import cpt202.groupwork.dto.MissionViewDTO;
import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Mission;
import cpt202.groupwork.entity.Project;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.GanttRepository;
import cpt202.groupwork.repository.MissionRepository;
import cpt202.groupwork.repository.ProjectRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.GanttService;
import cpt202.groupwork.service.TodolistService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GanttServicelmpl  implements GanttService {
  @Autowired
  GanttRepository ganttRepository;

  @Autowired
  MissionRepository missionRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Override
  public Response<?> getGantt(Integer projectId) {
    List<GanttViewDTO> ganttViewDTOs= new ArrayList<>();
    Optional<Project> project = projectRepository.findById(projectId);
    if (project.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "project not exist");
    }
    List<Gantt> gantts = ganttRepository.findByProjectId(projectId);
    for (Gantt gantt : gantts) {
      GanttViewDTO ganttViewDTO = new GanttViewDTO();
      BeanUtils.copyProperties(gantt, ganttViewDTO);
      ganttViewDTOs.add(ganttViewDTO);

      List<MissionViewDTO> missionViewDTOS = new ArrayList<>();
      List<Mission> missions = missionRepository.findByGanttId(gantt.getGanttId());

      for (Mission mission : missions) {
        MissionViewDTO missionViewDTO = new MissionViewDTO();
        BeanUtils.copyProperties(mission, missionViewDTO);
        missionViewDTOS.add(missionViewDTO);
      }
      ganttViewDTO.setMissionViewDTO(missionViewDTOS);
    }
    if(ganttViewDTOs.size() == 0) {
      GanttViewDTO ganttViewDTO = new GanttViewDTO();
      ganttViewDTO.setProjectId(projectId);
      ganttViewDTO.setGanttName("new Gantt");
      ganttViewDTO.setGanttTotalNum(0);
      Gantt newGantt = new Gantt();
      BeanUtils.copyProperties(ganttViewDTO, newGantt);
      ganttRepository.save(newGantt);
      ganttViewDTOs.add(ganttViewDTO);
    }
  return   Response.ok(ganttViewDTOs);

  }

  public Response<?> deleteGantt(Integer ganttId) {
    Optional<Gantt> gantt = ganttRepository.findById(ganttId);
    if (gantt.equals(Optional.empty())) {
      return Response.exceptionHandling(301, "gantt chart does not exist");
    }
    ganttRepository.deleteById(ganttId);
    List<Mission> missions = missionRepository.findByGanttId(ganttId);
    for (Mission mission : missions) {
      missionRepository.delete(mission);
    }
    return Response.ok();
  }
}