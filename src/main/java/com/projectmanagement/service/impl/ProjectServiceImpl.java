package com.projectmanagement.service.impl;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.ProjectManager;
import com.projectmanagement.entity.ProjectStatus;
import com.projectmanagement.exception.ResourceNotFoundException;
import com.projectmanagement.mapper.ProjectMapper;
import com.projectmanagement.repository.ProjectManagerRepository;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByProjectManagerId(Long pmId) {
        log.info("Fetching projects for project manager with ID: {}", pmId);

        // Validate if PM exists
        if (!projectManagerRepository.existsById(pmId)) {
            throw new ResourceNotFoundException("Project Manager not found with ID: " + pmId);
        }

        List<Project> projects = projectRepository.findByProjectManagerId(pmId);
        log.info("Found {} projects for project manager ID: {}", projects.size(), pmId);

        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getProjectsByProjectManagerId(Long pmId, Pageable pageable) {
        log.info("Fetching projects for project manager with ID: {} with pagination", pmId);

        if (!projectManagerRepository.existsById(pmId)) {
            throw new ResourceNotFoundException("Project Manager not found with ID: " + pmId);
        }

        Page<Project> projectPage = projectRepository.findByProjectManagerId(pmId, pageable);
        return projectPage.map(projectMapper::toProjectDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByProjectManagerEmail(String email) {
        log.info("Fetching projects for project manager with email: {}", email);

        List<Project> projects = projectRepository.findByProjectManagerEmail(email);
        log.info("Found {} projects for project manager email: {}", projects.size(), email);

        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByProjectManagerEmployeeId(String employeeId) {
        log.info("Fetching projects for project manager with employee ID: {}", employeeId);

        List<Project> projects = projectRepository.findByProjectManagerEmployeeId(employeeId);
        log.info("Found {} projects for project manager employee ID: {}", projects.size(), employeeId);

        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByProjectManagerIdAndStatus(Long pmId, ProjectStatus status) {
        log.info("Fetching projects for project manager ID: {} with status: {}", pmId, status);

        if (!projectManagerRepository.existsById(pmId)) {
            throw new ResourceNotFoundException("Project Manager not found with ID: " + pmId);
        }

        List<Project> projects = projectRepository.findByProjectManagerIdAndStatus(pmId, status);
        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByProjectManagerIdAndDateRange(Long pmId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching projects for project manager ID: {} between {} and {}", pmId, startDate, endDate);

        if (!projectManagerRepository.existsById(pmId)) {
            throw new ResourceNotFoundException("Project Manager not found with ID: " + pmId);
        }

        List<Project> projects = projectRepository.findByProjectManagerIdAndDateRange(pmId, startDate, endDate);
        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProjectsByProjectManagerId(Long pmId) {
        log.info("Counting projects for project manager ID: {}", pmId);

        if (!projectManagerRepository.existsById(pmId)) {
            throw new ResourceNotFoundException("Project Manager not found with ID: " + pmId);
        }

        return projectRepository.countProjectsByProjectManagerId(pmId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long projectId) {
        log.info("Fetching project with ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        return projectMapper.toProjectDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getProjectByCode(String projectCode) {
        log.info("Fetching project with code: {}", projectCode);

        Project project = projectRepository.findByProjectCodeAndIsActiveTrue(projectCode)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with code: " + projectCode));

        return projectMapper.toProjectDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllActiveProjects() {
        log.info("Fetching all active projects");

        List<Project> projects = projectRepository.findByIsActiveTrueOrderByUpdatedAtDesc();
        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsWithFilters(Long pmId, ProjectStatus status, String clientName) {
        log.info("Fetching projects with filters - PM ID: {}, Status: {}, Client: {}", pmId, status, clientName);

        List<Project> projects = projectRepository.findProjectsWithFilters(pmId, status, clientName);
        return projectMapper.toProjectDTOList(projects);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        log.info("Creating new project: {}", projectDTO.getName());

        Project project = projectMapper.toProject(projectDTO);
        Project savedProject = projectRepository.save(project);

        log.info("Created project with ID: {}", savedProject.getId());
        return projectMapper.toProjectDTO(savedProject);
    }

    @Override
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        log.info("Updating project with ID: {}", projectId);

        Project existingProject = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Update fields
        existingProject.setName(projectDTO.getName());
        existingProject.setDescription(projectDTO.getDescription());
        existingProject.setStatus(projectDTO.getStatus());
        existingProject.setPriority(projectDTO.getPriority());
        existingProject.setEndDate(projectDTO.getEndDate());
        existingProject.setBudget(projectDTO.getBudget());
        existingProject.setActualCost(projectDTO.getActualCost());
        existingProject.setCompletionPercentage(projectDTO.getCompletionPercentage());

        Project updatedProject = projectRepository.save(existingProject);
        return projectMapper.toProjectDTO(updatedProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        log.info("Deleting project with ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Soft delete
        project.setIsActive(false);
        projectRepository.save(project);

        log.info("Project with ID: {} has been soft deleted", projectId);
    }

    @Override
    public void assignProjectManagerToProject(Long projectId, Long pmId) {
        log.info("Assigning project manager ID: {} to project ID: {}", pmId, projectId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        ProjectManager pm = projectManagerRepository.findById(pmId)
            .orElseThrow(() -> new ResourceNotFoundException("Project Manager not found with ID: " + pmId));

        project.addProjectManager(pm);
        projectRepository.save(project);

        log.info("Successfully assigned PM ID: {} to project ID: {}", pmId, projectId);
    }

    @Override
    public void removeProjectManagerFromProject(Long projectId, Long pmId) {
        log.info("Removing project manager ID: {} from project ID: {}", pmId, projectId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        ProjectManager pm = projectManagerRepository.findById(pmId)
            .orElseThrow(() -> new ResourceNotFoundException("Project Manager not found with ID: " + pmId));

        project.removeProjectManager(pm);
        projectRepository.save(project);

        log.info("Successfully removed PM ID: {} from project ID: {}", pmId, projectId);
    }
}