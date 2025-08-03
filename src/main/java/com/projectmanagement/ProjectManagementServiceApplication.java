package com.projectmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ProjectManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagementServiceApplication.class, args);
    }
}