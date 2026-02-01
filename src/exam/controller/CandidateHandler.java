package exam.controller;

import exam.exception.DatabaseException;
import exam.model.Candidate;
import exam.repository.ICandidateRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CandidateHandler implements HttpHandler {
    private final ICandidateRepository candidateRepository;
    private final Gson gson;

    public CandidateHandler(ICandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
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
                case "DELETE" -> handleDelete(exchange); // НОВЫЙ МЕТОД
                default -> sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
            }
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "{\"error\": \"Invalid JSON syntax\"}");
        } catch (DatabaseException e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"Database error\"}");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"Internal Server Error\"}");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Candidate> list = candidateRepository.getAllCandidates();
        sendResponse(exchange, 200, gson.toJson(list));
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Candidate temp = gson.fromJson(body, Candidate.class);
        if (temp == null || temp.getName() == null || temp.getName().trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\": \"Name is required\"}");
            return;
        }
        candidateRepository.addCandidate(temp);
        sendResponse(exchange, 201, "{\"status\": \"Created\"}");
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Candidate temp = gson.fromJson(body, Candidate.class);
        if (temp == null || temp.getName() == null) {
            sendResponse(exchange, 400, "{\"error\": \"Name is required\"}");
            return;
        }
        candidateRepository.updateCandidate(temp.getName(), temp);
        sendResponse(exchange, 200, "{\"status\": \"Updated\"}");
    }

    // ОБРАБОТКА УДАЛЕНИЯ
    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("name=")) {
            String name = query.split("=")[1];
            // Декодируем имя (на случай пробелов %20)
            name = java.net.URLDecoder.decode(name, StandardCharsets.UTF_8);

            candidateRepository.deleteCandidate(name);
            sendResponse(exchange, 200, "{\"status\": \"Deleted\"}");
        } else {
            sendResponse(exchange, 400, "{\"error\": \"Name parameter is required\"}");
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