package main.java;

import main.java.api.controller.TaskController;
import main.java.api.service.TaskService;
import main.java.domain.entity.Task;
import main.java.domain.repository.TaskRepository;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        TaskRepository taskRepository = new TaskRepository();
        TaskService taskService = new TaskService(taskRepository);
        TaskController taskController = new TaskController(taskService);

        taskController.startServer();


    }
}