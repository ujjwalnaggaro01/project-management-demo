package com.projectmanagement.repository;

import com.projectmanagement.entity.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Long> {

    Optional<ProjectManager> findByEmailAndIsActiveTrue(String email);

    Optional<ProjectManager> findByEmployeeIdAndIsActiveTrue(String employeeId);

    List<ProjectManager> findByIsActiveTrueOrderByName();

    List<ProjectManager> findByDepartmentAndIsActiveTrue(String department);

    @Query("SELECT pm FROM ProjectManager pm WHERE " +
           "(:name IS NULL OR LOWER(pm.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:department IS NULL OR pm.department = :department) AND " +
           "pm.isActive = true")
    List<ProjectManager> findWithFilters(@Param("name") String name, 
                                        @Param("department") String department);

    boolean existsByEmailAndIsActiveTrue(String email);

    boolean existsByEmployeeIdAndIsActiveTrue(String employeeId);
}