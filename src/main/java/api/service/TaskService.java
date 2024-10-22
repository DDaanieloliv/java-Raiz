package main.java.api.service;

import main.java.domain.entity.Task;
import main.java.domain.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {


    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public void registerTask(String description, LocalDateTime dateTimeFrame) {
        var task = new Task(description, dateTimeFrame);
        taskRepository.save(task);
    }

    public List<Task> listAllTasks(){
        return taskRepository.listTasks();
    }
}
