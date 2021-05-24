package cpt202.groupwork.service;
import cpt202.groupwork.Response;
import cpt202.groupwork.dto.GanttViewDTO;
import cpt202.groupwork.dto.TodolistViewDTO;
import java.util.List;
public interface GanttService {
  List<GanttViewDTO> getGantt(Integer projectId);
  Response<?> deleteGantt(Integer ganttId);
}
