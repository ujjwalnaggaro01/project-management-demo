-- PROJECT MANAGERS TABLE
CREATE TABLE project_managers (
    pm_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(32),
    department VARCHAR(100),
    employee_id VARCHAR(100) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- PROJECTS TABLE
CREATE TABLE projects (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    project_code VARCHAR(20) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,
    priority VARCHAR(32),
    start_date DATE NOT NULL,
    end_date DATE,
    estimated_end_date DATE,
    budget DECIMAL(15,2),
    actual_cost DECIMAL(15,2),
    client_name VARCHAR(255),
    technology_stack VARCHAR(255),
    team_size INT,
    completion_percentage INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- PROJECT_MANAGER_ASSIGNMENTS: Join Table for Many-to-Many
CREATE TABLE project_manager_assignments (
    project_id BIGINT NOT NULL,
    pm_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, pm_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (pm_id) REFERENCES project_managers(pm_id) ON DELETE CASCADE
);

-- OPTIONAL: Add indexes for performance on foreign keys
CREATE INDEX idx_assignments_pm_id ON project_manager_assignments(pm_id);
CREATE INDEX idx_assignments_project_id ON project_manager_assignments(project_id);



INSERT INTO project_managers (name, email, phone, department, employee_id, is_active)
VALUES
  ('Alice Smith', 'alice.smith@company.com', '555-1001', 'Engineering', 'EMP1001', TRUE),
  ('Bob Johnson', 'bob.johnson@company.com', '555-1002', 'Product', 'EMP1002', TRUE),
  ('Charlie Lee', 'charlie.lee@company.com', '555-1003', 'Engineering', 'EMP1003', TRUE),
  ('Diana Patel', 'diana.patel@company.com', '555-1004', 'QA', 'EMP1004', TRUE),
  ('Ethan Brown', 'ethan.brown@company.com', '555-1005', 'Support', 'EMP1005', TRUE);


INSERT INTO projects (name, description, project_code, status, priority, start_date, end_date, budget, actual_cost, client_name, technology_stack, team_size, completion_percentage, is_active)
VALUES
  ('Website Redesign', 'Redesign the main website',       'PRJ1001', 'PLANNING',    'HIGH',    '2024-01-10', NULL,         15000,    NULL,     'Acme Corp', 'Spring,React',      8,   5,  TRUE),
  ('Mobile App',       'New customer mobile application',  'PRJ1002', 'IN_PROGRESS', 'CRITICAL', '2024-02-01', NULL,        30000,    11000,    'Beta Ltd',  'Flutter,Firebase',   12,  30, TRUE),
  ('Backend Overhaul', 'Refactor backend API',            'PRJ1003', 'PLANNING',    'MEDIUM',   '2024-02-20', NULL,        18000,    3000,     'Gamma Inc', 'Spring',            7,   10, TRUE),
  ('Microservice Integration', 'Integrate 3 services',    'PRJ1004', 'COMPLETED',   'HIGH',     '2023-10-15', '2023-12-20', 7000,    6500,     'Acme Corp', 'Spring',            5,   100, TRUE),
  ('QA Automation',    'Automate all QA flows',           'PRJ1005', 'IN_PROGRESS', 'MEDIUM',   '2024-03-05', NULL,        9000,     5000,     'Beta Ltd',  'Java,Selenium',      4,   40, TRUE),
  ('Support Portal',   'Deploy support portal',           'PRJ1006', 'ON_HOLD',     'LOW',      '2024-01-05', NULL,        6000,     NULL,     'Gamma Inc', 'PHP,Laravel',        3,   20, TRUE),
  ('Legacy Migration', 'Migrate legacy system',           'PRJ1007', 'IN_PROGRESS', 'CRITICAL', '2024-02-22', NULL,        25000,    6000,     'OldBank',   'Spring,PostgreSQL',  10,  25, TRUE),
  ('Cloud Move',       'Migrate infra to cloud',          'PRJ1008', 'PLANNING',    'CRITICAL', '2024-03-15', NULL,        40000,    NULL,     'Digitech',  'AWS,Docker',         7,   0,  TRUE),
  ('Analytics Suite',  'Business analytics tool',         'PRJ1009', 'IN_PROGRESS', 'HIGH',     '2024-01-28', NULL,        50000,    25000,    'BigData',   'Python,Spark',       14,  60, TRUE),
  ('Content Platform', 'Platform for content creators',   'PRJ1010', 'PLANNING',    'MEDIUM',   '2024-04-01', NULL,        17500,    NULL,     'UbuTube',   'Node,React',         9,   0,  TRUE);


INSERT INTO project_manager_assignments (project_id, pm_id) VALUES
  (1, 1),
  (1, 3),
  (2, 2),
  (2, 3),
  (3, 3),
  (3, 4),
  (4, 1),
  (4, 2),
  (5, 4),
  (5, 1),
  (6, 5),
  (7, 1),
  (7, 2),
  (7, 5),
  (8, 3),
  (8, 5),
  (9, 2),
  (9, 4),
  (10, 1),
  (10, 5);

