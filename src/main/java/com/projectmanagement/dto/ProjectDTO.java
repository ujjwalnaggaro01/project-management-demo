package com.projectmanagement.dto;

import com.projectmanagement.entity.ProjectPriority;
import com.projectmanagement.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String projectCode;
    private ProjectStatus status;
    private ProjectPriority priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate estimatedEndDate;
    private BigDecimal budget;
    private BigDecimal actualCost;
    private String clientName;
    private String technologyStack;
    private Integer teamSize;
    private Integer completionPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<ProjectManagerDTO> projectManagers;
}