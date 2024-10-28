package main.java.domain.repository;

import main.java.domain.entity.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/mydb";
    private final String JDBC_USER = "daniel";
    private final String JDBC_PASSWORD = "Daniel1234567!";

    /*
    __DriverManager é uma classe central do JDBC que gerencia um conjunto de drivers de banco
    de dados. Esses drivers são usados para abrir conexões com bancos de dados.

    __DriverManager.getConnection(...): Esse método tenta encontrar um driver JDBC registrado
    que corresponda à URL do banco de dados fornecida e, em seguida, usa esse driver para
    estabelecer uma conexão com o banco de dados. Ele retorna um objeto Connection, que
    representa a conexão ativa com o banco de dados.

    __O Connection é uma interface do JDBC que representa a conexão com o banco de dados.
    Através dessa conexão, você pode executar comandos SQL, como consultas (SELECT), inserções
    (INSERT), atualizações (UPDATE), e exclusões (DELETE), bem como gerenciar transações.

    ++ createStatement(): Cria um objeto Statement para enviar consultas SQL simples.

    ++ prepareStatement(String sql): Cria um objeto PreparedStatement, que é uma versão mais
    avançada do Statement e permite parametrizar consultas SQL (evitando SQL Injection e
    melhorando o desempenho em consultas repetidas).

    ++ close(): Fecha a conexão com o banco de dados. Isso é importante para liberar recursos.


    __O Statement é uma interface que permite enviar comandos SQL ao banco de dados. Ele é
    usado para executar consultas SQL estáticas, ou seja, consultas que não mudam ou não
    dependem de parâmetros externos.

    __O PreparedStatement é uma subinterface de Statement que permite consultar o banco de
    dados de forma mais eficiente e segura. Ele é usado para consultas parametrizadas, ou seja,
    consultas que contêm placeholders (?) e que podem ser preenchidas com valores
    posteriormente.


    __O ResultSet é uma interface que representa o resultado de uma consulta SQL (geralmente
    uma consulta SELECT). Ele contém todas as linhas que foram retornadas pela consulta e
    permite que você itere sobre essas linhas para acessar os dados.
    */

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
