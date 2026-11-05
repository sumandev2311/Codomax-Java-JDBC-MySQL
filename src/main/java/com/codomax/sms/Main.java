package com.codomax.sms;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final StudentDAO STUDENTS = new StudentDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     STUDENT MANAGEMENT SYSTEM");
        System.out.println("       Java + MySQL + JDBC");
        System.out.println("========================================");

        try {
            // Verify database connectivity before showing the menu.
            try (var connection = DBConnection.getConnection()) {
                System.out.println("Connected to MySQL successfully.");
            }
        } catch (SQLException exception) {
            System.err.println("Could not connect to MySQL: " + exception.getMessage());
            System.err.println("Check that MySQL is running, schema.sql has been executed,");
            System.err.println("and DB_URL, DB_USER and DB_PASSWORD are correct.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> listStudents();
                    case 3 -> searchStudent();
                    case 4 -> updateStudent();
                    case 5 -> deleteStudent();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (SQLException exception) {
                if (exception.getErrorCode() == 1062) {
                    System.out.println("That email address is already registered.");
                } else {
                    System.out.println("Database operation failed: " + exception.getMessage());
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid input: " + exception.getMessage());
            }
            System.out.println();
        }
        System.out.println("Thank you for using the Student Management System.");
    }

    private static void printMenu() {
        System.out.println("\n--------------- MENU ---------------");
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Search student by ID");
        System.out.println("4. Update student");
        System.out.println("5. Delete student");
        System.out.println("0. Exit");
        System.out.println("------------------------------------");
    }

    private static void addStudent() throws SQLException {
        String name = readRequired("Student name: ");
        String email = readRequired("Email: ");
        String course = readRequired("Course: ");
        BigDecimal marks = readMarks("Marks (0-100): ");
        int id = STUDENTS.addStudent(new Student(name, email, course, marks));
        System.out.println("Student added successfully. New student ID: " + id);
    }

    private static void listStudents() throws SQLException {
        List<Student> students = STUDENTS.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        System.out.println("\nStudent records:");
        students.forEach(System.out::println);
    }

    private static void searchStudent() throws SQLException {
        int id = readInt("Enter student ID: ");
        Student student = STUDENTS.findById(id);
        System.out.println(student == null ? "Student not found." : student);
    }

    private static void updateStudent() throws SQLException {
        int id = readInt("Enter student ID to update: ");
        Student existing = STUDENTS.findById(id);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Current record: " + existing);
        String name = readRequired("New name: ");
        String email = readRequired("New email: ");
        String course = readRequired("New course: ");
        BigDecimal marks = readMarks("New marks (0-100): ");
        boolean updated = STUDENTS.updateStudent(new Student(id, name, email, course, marks));
        System.out.println(updated ? "Student updated successfully." : "No changes were made.");
    }

    private static void deleteStudent() throws SQLException {
        int id = readInt("Enter student ID to delete: ");
        String confirmation = readRequired("Delete student " + id + "? (yes/no): ");
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Delete cancelled.");
            return;
        }
        System.out.println(STUDENTS.deleteStudent(id)
                ? "Student deleted successfully." : "Student not found.");
    }

    private static String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            try {
                int number = Integer.parseInt(value);
                if (number >= 0) return number;
            } catch (NumberFormatException ignored) {
                // Ask again below.
            }
            System.out.println("Enter a valid non-negative whole number.");
        }
    }

    private static BigDecimal readMarks(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                BigDecimal marks = new BigDecimal(INPUT.nextLine().trim());
                if (marks.compareTo(BigDecimal.ZERO) >= 0
                        && marks.compareTo(new BigDecimal("100")) <= 0) {
                    return marks.setScale(2, java.math.RoundingMode.HALF_UP);
                }
            } catch (NumberFormatException ignored) {
                // Ask again below.
            }
            System.out.println("Enter a number from 0 to 100.");
        }
    }
}
