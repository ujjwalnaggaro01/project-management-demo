package com.projectmanagement.controller;

import com.projectmanagement.dto.ProjectDTO;
import com.projectmanagement.entity.ProjectStatus;
import com.projectmanagement.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Get all projects handled by a specific project manager (by PM ID)
     */
    @GetMapping("/pm/{pmId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectManagerId(
            @PathVariable Long pmId) {
        log.info("GET /projects/pm/{} - Fetching projects for project manager", pmId);

        List<ProjectDTO> projects = projectService.getProjectsByProjectManagerId(pmId);

        log.info("Found {} projects for project manager ID: {}", projects.size(), pmId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Get all projects handled by a specific project manager with pagination
     */
    @GetMapping("/pm/{pmId}/paginated")
    public ResponseEntity<Page<ProjectDTO>> getProjectsByProjectManagerIdPaginated(
            @PathVariable Long pmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("GET /projects/pm/{}/paginated - page: {}, size: {}", pmId, page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProjectDTO> projectPage = projectService.getProjectsByProjectManagerId(pmId, pageable);

        log.info("Found {} projects in page {} for project manager ID: {}", 
                projectPage.getContent().size(), page, pmId);
        return ResponseEntity.ok(projectPage);
    }

    /**
     * Get all projects handled by a specific project manager (by email)
     */
    @GetMapping("/pm/email/{email}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectManagerEmail(
            @PathVariable String email) {
        log.info("GET /projects/pm/email/{} - Fetching projects for project manager", email);

        List<ProjectDTO> projects = projectService.getProjectsByProjectManagerEmail(email);

        log.info("Found {} projects for project manager email: {}", projects.size(), email);
        return ResponseEntity.ok(projects);
    }

    /**
     * Get all projects handled by a specific project manager (by employee ID)
     */
    @GetMapping("/pm/employee/{employeeId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectManagerEmployeeId(
            @PathVariable String employeeId) {
        log.info("GET /projects/pm/employee/{} - Fetching projects for project manager", employeeId);

        List<ProjectDTO> projects = projectService.getProjectsByProjectManagerEmployeeId(employeeId);

        log.info("Found {} projects for project manager employee ID: {}", projects.size(), employeeId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Get projects by project manager and status
     */
    @GetMapping("/pm/{pmId}/status/{status}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectManagerIdAndStatus(
            @PathVariable Long pmId,
            @PathVariable ProjectStatus status) {
        log.info("GET /projects/pm/{}/status/{} - Fetching projects", pmId, status);

        List<ProjectDTO> projects = projectService.getProjectsByProjectManagerIdAndStatus(pmId, status);

        log.info("Found {} projects with status {} for PM ID: {}", projects.size(), status, pmId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Get projects by project manager within a date range
     */
    @GetMapping("/pm/{pmId}/daterange")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectManagerIdAndDateRange(
            @PathVariable Long pmId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("GET /projects/pm/{}/daterange - startDate: {}, endDate: {}", pmId, startDate, endDate);

        List<ProjectDTO> projects = projectService.getProjectsByProjectManagerIdAndDateRange(pmId, startDate, endDate);

        log.info("Found {} projects between {} and {} for PM ID: {}", 
                projects.size(), startDate, endDate, pmId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Count projects handled by a specific project manager
     */
    @GetMapping("/pm/{pmId}/count")
    public ResponseEntity<Long> countProjectsByProjectManagerId(@PathVariable Long pmId) {
        log.info("GET /projects/pm/{}/count - Counting projects for project manager", pmId);

        Long count = projectService.countProjectsByProjectManagerId(pmId);

        log.info("Project manager ID: {} handles {} projects", pmId, count);
        return ResponseEntity.ok(count);
    }

    /**
     * Get project by ID
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long projectId) {
        log.info("GET /projects/{} - Fetching project details", projectId);

        ProjectDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    /**
     * Get project by code
     */
    @GetMapping("/code/{projectCode}")
    public ResponseEntity<ProjectDTO> getProjectByCode(@PathVariable String projectCode) {
        log.info("GET /projects/code/{} - Fetching project details", projectCode);

        ProjectDTO project = projectService.getProjectByCode(projectCode);
        return ResponseEntity.ok(project);
    }

    /**
     * Get all active projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllActiveProjects() {
        log.info("GET /projects - Fetching all active projects");

        List<ProjectDTO> projects = projectService.getAllActiveProjects();

        log.info("Found {} active projects", projects.size());
        return ResponseEntity.ok(projects);
    }

    /**
     * Search projects with filters
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProjectDTO>> searchProjects(
            @RequestParam(required = false) Long pmId,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) String clientName) {

        log.info("GET /projects/search - pmId: {}, status: {}, client: {}", pmId, status, clientName);

        List<ProjectDTO> projects = projectService.getProjectsWithFilters(pmId, status, clientName);

        log.info("Found {} projects matching search criteria", projects.size());
        return ResponseEntity.ok(projects);
    }

    /**
     * Create a new project
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        log.info("POST /projects - Creating new project: {}", projectDTO.getName());

        ProjectDTO createdProject = projectService.createProject(projectDTO);

        log.info("Created project with ID: {}", createdProject.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    /**
     * Update an existing project
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectDTO projectDTO) {
        log.info("PUT /projects/{} - Updating project", projectId);

        ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO);

        log.info("Updated project with ID: {}", projectId);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Delete a project (soft delete)
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        log.info("DELETE /projects/{} - Deleting project", projectId);

        projectService.deleteProject(projectId);

        log.info("Deleted project with ID: {}", projectId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign project manager to project
     */
    @PostMapping("/{projectId}/pm/{pmId}")
    public ResponseEntity<Void> assignProjectManagerToProject(
            @PathVariable Long projectId,
            @PathVariable Long pmId) {
        log.info("POST /projects/{}/pm/{} - Assigning PM to project", projectId, pmId);

        projectService.assignProjectManagerToProject(projectId, pmId);

        log.info("Assigned PM ID: {} to project ID: {}", pmId, projectId);
        return ResponseEntity.ok().build();
    }

    /**
     * Remove project manager from project
     */
    @DeleteMapping("/{projectId}/pm/{pmId}")
    public ResponseEntity<Void> removeProjectManagerFromProject(
            @PathVariable Long projectId,
            @PathVariable Long pmId) {
        log.info("DELETE /projects/{}/pm/{} - Removing PM from project", projectId, pmId);

        projectService.removeProjectManagerFromProject(projectId, pmId);

        log.info("Removed PM ID: {} from project ID: {}", pmId, projectId);
        return ResponseEntity.ok().build();
    }
}