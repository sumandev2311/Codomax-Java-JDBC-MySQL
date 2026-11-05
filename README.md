# Java Database & JDBC – Student Management System

A beginner-friendly console application built with **Java 17, JDBC, Maven and MySQL** for the Codomax Java Database & JDBC internship module.

## Features

- Add, list, search, update and delete student records (CRUD).
- Uses JDBC and `PreparedStatement` for parameterized SQL.
- Uses try-with-resources to close database resources safely.
- Uses a database transaction when adding a student and its activity-log entry.
- Validates required fields and marks (0–100).
- Handles duplicate email addresses and database errors.

## Project structure

```text
database/schema.sql
src/main/java/com/codomax/sms/
    DBConnection.java
    Main.java
    Student.java
    StudentDAO.java
pom.xml
```

## Requirements

- JDK 17 or later
- MySQL Server 8.0 or later
- MySQL Workbench (optional, for running the SQL script visually)
- Maven 3.8+ (or IntelliJ IDEA with Maven support)

## 1. Create the database

Start MySQL Server. Open MySQL Workbench, connect to your local server, open `database/schema.sql`, and execute the script.

Alternatively, run it from a terminal:

```bash
mysql -u root -p < database/schema.sql
```

The script creates the `student_management` database, `students` table and `activity_log` table, plus two sample students.

## 2. Configure the database login

The application reads these environment variables:

| Variable | Default | Meaning |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` | JDBC connection URL |
| `DB_USER` | `root` | MySQL username |
| `DB_PASSWORD` | empty | MySQL password |

Set `DB_PASSWORD` to the password you chose when installing MySQL. Do not commit real passwords to GitHub.

**Windows PowerShell (for the current terminal):**

```powershell
$env:DB_USER = "root"
$env:DB_PASSWORD = "your_mysql_password"
```

**macOS/Linux:**

```bash
export DB_USER=root
export DB_PASSWORD='your_mysql_password'
```

If your MySQL server uses a different port or database name, set `DB_URL` accordingly.

## 3. Run the application

From the project root:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.codomax.sms.Main"
```

If Maven's exec plugin is not available in your environment, open the project in IntelliJ IDEA, allow Maven to download the MySQL Connector/J dependency, then run `Main.java`.

## 4. Menu options

```text
1. Add student
2. View all students
3. Search student by ID
4. Update student
5. Delete student
0. Exit
```

## Example workflow

1. Run the SQL script.
2. Set your MySQL password in `DB_PASSWORD`.
3. Start the Java application.
4. Choose `1` to add a student.
5. Choose `2` to view records.
6. Use options `3`, `4` and `5` to search, update and delete.

## JDBC concepts demonstrated

- `DriverManager` and `Connection`
- `PreparedStatement` and `ResultSet`
- CRUD operations
- SQL parameter binding
- Transactions with `setAutoCommit`, `commit` and `rollback`
- Exception handling and try-with-resources

## Troubleshooting

- **Access denied:** verify `DB_USER` and `DB_PASSWORD`.
- **Connection refused:** make sure MySQL Server is running and the port in `DB_URL` is correct.
- **Unknown database:** execute `database/schema.sql`.
- **No suitable driver:** refresh/reload the Maven project so MySQL Connector/J is downloaded.

## Security note

This is an educational local CLI project. It uses parameterized statements to reduce SQL-injection risk. For a production system, add authentication, authorization, stronger input validation, secrets management, logging and automated tests.
