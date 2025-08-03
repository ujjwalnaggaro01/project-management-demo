package com.projectmanagement.repository;

import com.projectmanagement.entity.Project;
import com.projectmanagement.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find projects by project manager ID
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE pm.id = :pmId AND p.isActive = true")
    List<Project> findByProjectManagerId(@Param("pmId") Long pmId);

    // Find projects by project manager ID with pagination
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE pm.id = :pmId AND p.isActive = true")
    Page<Project> findByProjectManagerId(@Param("pmId") Long pmId, Pageable pageable);

    // Find projects by project manager email
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE pm.email = :email AND p.isActive = true")
    List<Project> findByProjectManagerEmail(@Param("email") String email);

    // Find projects by project manager employee ID
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE pm.employeeId = :employeeId AND p.isActive = true")
    List<Project> findByProjectManagerEmployeeId(@Param("employeeId") String employeeId);

    // Find projects by status and project manager
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE pm.id = :pmId AND p.status = :status AND p.isActive = true")
    List<Project> findByProjectManagerIdAndStatus(@Param("pmId") Long pmId, @Param("status") ProjectStatus status);

    // Find active projects within date range for a PM
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE pm.id = :pmId " +
           "AND p.startDate >= :startDate AND p.startDate <= :endDate AND p.isActive = true")
    List<Project> findByProjectManagerIdAndDateRange(@Param("pmId") Long pmId, 
                                                    @Param("startDate") LocalDate startDate, 
                                                    @Param("endDate") LocalDate endDate);

    // Count projects by PM
    @Query("SELECT COUNT(DISTINCT p) FROM Project p JOIN p.projectManagers pm WHERE pm.id = :pmId AND p.isActive = true")
    Long countProjectsByProjectManagerId(@Param("pmId") Long pmId);

    // Find by project code
    Optional<Project> findByProjectCodeAndIsActiveTrue(String projectCode);

    // Find all active projects
    List<Project> findByIsActiveTrueOrderByUpdatedAtDesc();

    // Custom query for complex filtering
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectManagers pm WHERE " +
           "(:pmId IS NULL OR pm.id = :pmId) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:clientName IS NULL OR LOWER(p.clientName) LIKE LOWER(CONCAT('%', :clientName, '%'))) AND " +
           "p.isActive = true ORDER BY p.updatedAt DESC")
    List<Project> findProjectsWithFilters(@Param("pmId") Long pmId, 
                                         @Param("status") ProjectStatus status,
                                         @Param("clientName") String clientName);
}