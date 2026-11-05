package com.codomax.sms;

import java.math.BigDecimal;

public class Student {
    private int id;
    private String name;
    private String email;
    private String course;
    private BigDecimal marks;

    public Student(int id, String name, String email, String course, BigDecimal marks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    public Student(String name, String email, String course, BigDecimal marks) {
        this(0, name, email, course, marks);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public BigDecimal getMarks() { return marks; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
    public void setMarks(BigDecimal marks) { this.marks = marks; }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Email: %s | Course: %s | Marks: %s",
                id, name, email, course, marks.toPlainString());
    }
}
