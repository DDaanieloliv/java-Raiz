package main.java.domain.repository;

import main.java.domain.entity.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/mydb";
    private final String JDBC_USER = "daniel";
    private final String JDBC_PASSWORD = "Daniel1234567!";

    public TaskRepository () {
        try (Connection connection = DriverManager
                .getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){


            Statement statement = connection.createStatement();
            statement
                    .executeUpdate("CREATE TABLE IF NOT EXISTS task (" +
                            "id SERIAL PRIMARY KEY, " +
                            "description VARCHAR(255), " +
                            "dataTimeFrame TIMESTAMP NOT NULL, " +
                            "currentDatePosting TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void save(Task task) {
        try (Connection connection = DriverManager
                .getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){

                String query = "INSERT INTO task (description, dateTimeFrame) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)){
                    statement.setString(1, task.getDescription());
                    statement.setString(2, String.valueOf(task.getDatetimeFrame()));
                    statement.executeUpdate();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> listTasks() {

        List<Task> tasks = new ArrayList<>();
        try (Connection connection = DriverManager
                .getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            String query = "SELECT description, dateTimeFrame, currentDatePosting FROM task;";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)){
                while (resultSet.next()){
                    tasks.add(new Task(
                            resultSet.getString("id"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("datetimeFrame"),
                            resultSet.getTimestamp("currentDatePosting")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
