package com.projectmanagement.mapper;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.dto.ProjectManagerDTO;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.ProjectManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public ProjectDTO toProjectDTO(Project project) {
        if (project == null) return null;

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setProjectCode(project.getProjectCode());
        dto.setStatus(project.getStatus());
        dto.setPriority(project.getPriority());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setEstimatedEndDate(project.getEstimatedEndDate());
        dto.setBudget(project.getBudget());
        dto.setActualCost(project.getActualCost());
        dto.setClientName(project.getClientName());
        dto.setTechnologyStack(project.getTechnologyStack());
        dto.setTeamSize(project.getTeamSize());
        dto.setCompletionPercentage(project.getCompletionPercentage());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());

        // Map project managers
        Set<ProjectManagerDTO> pmDTOs = project.getProjectManagers().stream()
            .map(this::toProjectManagerDTO)
            .collect(Collectors.toSet());
        dto.setProjectManagers(pmDTOs);

        return dto;
    }

    public Project toProject(ProjectDTO dto) {
        if (dto == null) return null;

        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setProjectCode(dto.getProjectCode());
        project.setStatus(dto.getStatus());
        project.setPriority(dto.getPriority());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setEstimatedEndDate(dto.getEstimatedEndDate());
        project.setBudget(dto.getBudget());
        project.setActualCost(dto.getActualCost());
        project.setClientName(dto.getClientName());
        project.setTechnologyStack(dto.getTechnologyStack());
        project.setTeamSize(dto.getTeamSize());
        project.setCompletionPercentage(dto.getCompletionPercentage());

        return project;
    }

    public List<ProjectDTO> toProjectDTOList(List<Project> projects) {
        return projects.stream()
            .map(this::toProjectDTO)
            .collect(Collectors.toList());
    }

    private ProjectManagerDTO toProjectManagerDTO(ProjectManager pm) {
        if (pm == null) return null;

        ProjectManagerDTO dto = new ProjectManagerDTO();
        dto.setId(pm.getId());
        dto.setName(pm.getName());
        dto.setEmail(pm.getEmail());
        dto.setPhone(pm.getPhone());
        dto.setDepartment(pm.getDepartment());
        dto.setEmployeeId(pm.getEmployeeId());
        dto.setIsActive(pm.getIsActive());
        dto.setCreatedAt(pm.getCreatedAt());
        dto.setUpdatedAt(pm.getUpdatedAt());

        return dto;
    }
}