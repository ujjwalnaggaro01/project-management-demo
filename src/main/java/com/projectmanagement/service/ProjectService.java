package com.projectmanagement.service;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getProjectsByProjectManagerId(Long pmId);

    Page<ProjectDTO> getProjectsByProjectManagerId(Long pmId, Pageable pageable);

    List<ProjectDTO> getProjectsByProjectManagerEmail(String email);

    List<ProjectDTO> getProjectsByProjectManagerEmployeeId(String employeeId);

    List<ProjectDTO> getProjectsByProjectManagerIdAndStatus(Long pmId, ProjectStatus status);

    List<ProjectDTO> getProjectsByProjectManagerIdAndDateRange(Long pmId, LocalDate startDate, LocalDate endDate);

    Long countProjectsByProjectManagerId(Long pmId);

    ProjectDTO getProjectById(Long projectId);

    ProjectDTO getProjectByCode(String projectCode);

    List<ProjectDTO> getAllActiveProjects();

    List<ProjectDTO> getProjectsWithFilters(Long pmId, ProjectStatus status, String clientName);

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);

    void deleteProject(Long projectId);

    void assignProjectManagerToProject(Long projectId, Long pmId);

    void removeProjectManagerFromProject(Long projectId, Long pmId);
}