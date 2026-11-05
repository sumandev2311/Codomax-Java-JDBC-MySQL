-- Run this script in MySQL Workbench or the MySQL command-line client.
CREATE DATABASE IF NOT EXISTS student_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE student_management;

CREATE TABLE IF NOT EXISTS students (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL,
    marks DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_student_marks CHECK (marks >= 0 AND marks <= 100)
);

CREATE TABLE IF NOT EXISTS activity_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT NULL,
    action VARCHAR(30) NOT NULL,
    details VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activity_student
        FOREIGN KEY (student_id) REFERENCES students(id)
        ON DELETE SET NULL
);

-- Optional sample records:
INSERT INTO students (name, email, course, marks)
VALUES
    ('Aarav Sharma', 'aarav@example.com', 'Computer Science', 86.50),
    ('Diya Sen', 'diya@example.com', 'Information Technology', 91.00)
ON DUPLICATE KEY UPDATE email = VALUES(email);
