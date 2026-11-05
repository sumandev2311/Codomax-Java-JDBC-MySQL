package com.codomax.sms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public int addStudent(Student student) throws SQLException {
        String insertStudent = "INSERT INTO students(name, email, course, marks) VALUES (?, ?, ?, ?)";
        String insertLog = "INSERT INTO activity_log(student_id, action, details) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int studentId;
                try (PreparedStatement statement = connection.prepareStatement(
                        insertStudent, Statement.RETURN_GENERATED_KEYS)) {
                    bindStudent(statement, student);
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Creating student failed; no ID was returned.");
                        }
                        studentId = keys.getInt(1);
                    }
                }

                try (PreparedStatement log = connection.prepareStatement(insertLog)) {
                    log.setInt(1, studentId);
                    log.setString(2, "CREATE");
                    log.setString(3, "Created student: " + student.getName());
                    log.executeUpdate();
                }

                connection.commit();
                return studentId;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT id, name, email, course, marks FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                students.add(mapStudent(result));
            }
        }
        return students;
    }

    public Student findById(int id) throws SQLException {
        String sql = "SELECT id, name, email, course, marks FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? mapStudent(result) : null;
            }
        }
    }

    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, email = ?, course = ?, marks = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindStudent(statement, student);
            statement.setInt(5, student.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private void bindStudent(PreparedStatement statement, Student student) throws SQLException {
        statement.setString(1, student.getName());
        statement.setString(2, student.getEmail());
        statement.setString(3, student.getCourse());
        statement.setBigDecimal(4, student.getMarks());
    }

    private Student mapStudent(ResultSet result) throws SQLException {
        return new Student(
                result.getInt("id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("course"),
                result.getBigDecimal("marks")
        );
    }
}
