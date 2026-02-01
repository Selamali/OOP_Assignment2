package exam.controller;

import exam.model.Candidate;
import exam.model.Question;
import exam.repository.ICandidateRepository;
import exam.repository.IQuestionRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExamHandler implements HttpHandler {
    private final IQuestionRepository qRepo;
    private final ICandidateRepository cRepo;
    private final Gson gson;

    public ExamHandler(IQuestionRepository qRepo, ICandidateRepository cRepo) {
        this.qRepo = qRepo;
        this.cRepo = cRepo;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            if (path.endsWith("/generate") && method.equals("GET")) {
                handleGenerateExam(exchange);
            } else if (path.endsWith("/submit") && method.equals("POST")) {
                handleSubmitExam(exchange);
            } else {
                sendResponse(exchange, 404, "{\"error\": \"Not Found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleGenerateExam(HttpExchange exchange) throws IOException {
        // Генерируем 5 случайных вопросов
        List<Question> examQuestions = qRepo.getRandomQuestions(5);
        sendResponse(exchange, 200, gson.toJson(examQuestions));
    }

    private void handleSubmitExam(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        String candidateName = json.get("candidateName").getAsString();
        int totalScore = 0;
        int maxScore = 0;

        // Эмуляция проверки ответов (MVP logic)
        // В реальном продукте мы бы сравнивали с correct_answer из БД
        if (json.has("answers")) {
            for (var entry : json.getAsJsonArray("answers")) {
                JsonObject answerObj = entry.getAsJsonObject();
                int qId = answerObj.get("id").getAsInt();
                String answerText = answerObj.get("answer").getAsString();

                // Получаем вопрос, чтобы узнать его стоимость
                Question q = qRepo.getQuestionById(qId);
                if (q != null) {
                    maxScore += q.getMarks();
                    // Логика проверки: если ответ длиннее 5 символов -> засчитываем (для демо)
                    if (answerText != null && answerText.trim().length() > 5) {
                        totalScore += q.getMarks();
                    }
                }
            }
        }

        // Ограничиваем максимум 100 баллов
        if (totalScore > 100) totalScore = 100;

        // Обновляем кандидата
        Candidate c = cRepo.getCandidateByName(candidateName);
        if (c != null) {
            c.setScore(totalScore);
            // Если набрал > 50%, считаем что сдал
            // (Логика обновления статуса Passed/Failed на фронте зависит от баллов)
            cRepo.updateCandidate(candidateName, c);
        }

        String response = String.format("{\"status\": \"success\", \"score\": %d, \"max\": %d}", totalScore, maxScore);
        sendResponse(exchange, 200, response);
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) body.append(line);
            return body.toString();
        }
    }

    private void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }
}