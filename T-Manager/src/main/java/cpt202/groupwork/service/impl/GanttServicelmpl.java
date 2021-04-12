package cpt202.groupwork.service.impl;
//GanttServicelmpl
import cpt202.groupwork.dto.GanttViewDTO;
import cpt202.groupwork.dto.MissionViewDTO;
import cpt202.groupwork.dto.TodoViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import cpt202.groupwork.entity.Gantt;
import cpt202.groupwork.entity.Mission;
import cpt202.groupwork.entity.Todo;
import cpt202.groupwork.entity.Todolist;
import cpt202.groupwork.repository.GanttRepository;
import cpt202.groupwork.repository.MissionRepository;
import cpt202.groupwork.repository.TodolistRepository;
import cpt202.groupwork.repository.TodoRepository;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.GanttService;
import cpt202.groupwork.service.TodolistService;
import java.util.ArrayList;
import java.util.List;
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

  @Override
  public List<GanttViewDTO> getGantt(Integer projectId) {
    List<GanttViewDTO> ganttViewDTOs= new ArrayList<>();
    List<Gantt> gantts = ganttRepository.findByProjectId(projectId);

    for (Gantt gantt : gantts) {
      GanttViewDTO ganttViewDTO = new GanttViewDTO();
      BeanUtils.copyProperties(gantt, ganttViewDTO);
//      ganttViewDTO.setAvatar(userRepository.findAvatarByUsername(ganttViewDTO.getUsername()));
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

    return ganttViewDTOs;

  }
}