package main.java.api.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.java.api.service.TaskService;
import main.java.domain.entity.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.List;

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/register", this::handleRegisterUser);

        server.createContext("/task",  this::handleListTasks);

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080/");
    }


    private void handleListTasks(HttpExchange exchange) throws IOException {

        if("GET".equals(exchange.getRequestMethod())) {
            List<Task> tasks = taskService.listAllTasks();
            StringBuilder response = new StringBuilder();

            for (Task task : tasks) {
                response.append("ID: ").append(task.getId())
                        .append(", Description: ").append(task.getDescription())
                        .append(", DateTimeFrame: ").append(task.getDatetimeFrame())
                        .append(", CurrentDatePosting: ").append(task.getCurrentDatePosting())
                        .append("\n");
            }

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }

    }

    private void handleRegisterUser(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String query = new String(exchange.getRequestBody().readAllBytes());

            String[] params = query.split("&");
            String description = params[0].split("=")[1];
            String dateTimeString = params[1].split("=")[1];

            LocalDateTime datetimeFrame = LocalDateTime.parse(dateTimeString);

            taskService.registerTask(description, datetimeFrame);

            String response = "Task '" + description + "' registered successfully!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

}
