package exam.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class WebHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();

        // Если просят корень "/", отдаем index.html
        if (requestPath.equals("/")) {
            requestPath = "index.html";
        } else {
            // Убираем начальный слэш, чтобы искать файл относительно папки проекта
            // Например: "/script.js" -> "script.js"
            requestPath = requestPath.substring(1);
        }

        // Логика поиска файла (как в прошлом фиксе, но теперь для любого файла)
        String[] possiblePaths = {requestPath, "src/" + requestPath, "../" + requestPath};
        File file = null;

        for (String path : possiblePaths) {
            File candidate = new File(path);
            if (candidate.exists() && !candidate.isDirectory()) {
                file = candidate;
                break;
            }
        }

        if (file != null) {
            // Определяем тип контента (HTML, CSS, JS, PNG и т.д.)
            String contentType = guessContentType(file.getName());
            exchange.getResponseHeaders().set("Content-Type", contentType);

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            // Честный 404, если файл не найден
            String response = "404 Not Found: " + requestPath;
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // Простой метод для определения типа файла
    private String guessContentType(String fileName) {
        if (fileName.endsWith(".html")) return "text/html; charset=UTF-8";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
}