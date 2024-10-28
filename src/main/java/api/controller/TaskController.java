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


    /*
    Definição de Contextos (createContext):

        /register:
        Define um endpoint para registrar novas tarefas. Quando uma requisição é
        feita para http://localhost:8080/register, o método handleRegisterUser será chamado
        para processar a requisição.

        /task:
        Define um endpoint para listar todas as tarefas. Quando uma requisição é feita para
        http://localhost:8080/task, o método handleListTasks será chamado para processar a
        requisição.

        server.setExecutor(null):
            Define o executor como null, o que significa que o servidor usará um executor
            padrão (um único thread) para processar as requisições.

        server.start(): Inicia o servidor HTTP.

    */

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/register", this::handleRegisterUser);

        server.createContext("/task",  this::handleListTasks);

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080/");
    }


    /*
    HttpExchange:
        O objeto HttpExchange contém todas as informações sobre a requisição HTTP (como o
        método HTTP, cabeçalhos, corpo da requisição, etc.) e permite que você envie uma
        resposta de volta ao cliente.

    O método sendResponseHeaders envia o código de status HTTP 200 (OK) e o tamanho da
    resposta em bytes.

    O corpo da resposta é escrito no OutputStream do HttpExchange para ser enviado de volta
    ao cliente.
    */

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



    /*
    O método lê o corpo da requisição HTTP, que contém os dados enviados pelo cliente.
    Ele usa exchange.getRequestBody().readAllBytes() para capturar os dados brutos e os
    converte para uma string.

    A string query contém os parâmetros enviados pelo cliente (provavelmente no formato
    description=...&dateTimeFrame=...). Esses parâmetros são divididos e extraídos:

    description: O valor da descrição da tarefa.

    dateTimeString: O valor do timestamp da tarefa (como string).

    O dateTimeString é convertido para um LocalDateTime (formato de data e hora) usando
    LocalDateTime.parse(dateTimeString).

    O método chama taskService.registerTask(description, datetimeFrame) para registrar a nova
    tarefa. A camada de serviço se encarregará de salvar a tarefa no banco de dados (através
    do repositório).
    */
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
