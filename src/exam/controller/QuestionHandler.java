package exam.controller;

import exam.exception.DatabaseException;
import exam.model.Question;
import exam.repository.IQuestionRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class QuestionHandler implements HttpHandler {
    private final IQuestionRepository questionRepository;
    private final Gson gson;

    public QuestionHandler(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // CORS заголовки
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            switch (method) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "PUT" -> handlePut(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
            }
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "{\"error\": \"Invalid JSON syntax\"}");
        } catch (DatabaseException e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"Database error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"Internal Server Error\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            try {
                int id = Integer.parseInt(query.split("=")[1]);
                Question q = questionRepository.getQuestionById(id);
                if (q != null) {
                    sendResponse(exchange, 200, gson.toJson(q));
                } else {
                    sendResponse(exchange, 404, "{\"error\": \"Question not found\"}");
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"error\": \"Invalid ID format\"}");
            }
        } else {
            List<Question> list = questionRepository.getAllQuestions();
            sendResponse(exchange, 200, gson.toJson(list));
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Question temp = gson.fromJson(body, Question.class);

        if (temp == null || temp.getText() == null || temp.getText().trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\": \"Field 'text' is required\"}");
            return;
        }

        // ОБНОВЛЕНО: Передаем категорию (temp.getCategory())
        questionRepository.addQuestion(temp.getText(), temp.getMarks(), temp.getCategory());

        sendResponse(exchange, 201, "{\"status\": \"Created\"}");
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Question temp = gson.fromJson(body, Question.class);

        if (temp == null || temp.getId() == 0) {
            sendResponse(exchange, 400, "{\"error\": \"ID is required for update\"}");
            return;
        }

        // ОБНОВЛЕНО: Передаем категорию (temp.getCategory())
        questionRepository.updateQuestion(temp.getId(), temp.getText(), temp.getMarks(), temp.getCategory());

        sendResponse(exchange, 200, "{\"status\": \"Updated\"}");
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            try {
                int id = Integer.parseInt(query.split("=")[1]);
                questionRepository.deleteQuestion(id);
                sendResponse(exchange, 200, "{\"status\": \"Deleted\"}");
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"error\": \"Invalid ID format\"}");
            }
        } else {
            sendResponse(exchange, 400, "{\"error\": \"ID parameter is required\"}");
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        }
    }

    private void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}