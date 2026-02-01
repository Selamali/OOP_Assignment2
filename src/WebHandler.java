import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class WebHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Мы отдаем index.html только на запрос "/"
        // Все остальные запросы (например, style.css) мы пока игнорируем для простоты

        File file = new File("src/index.html"); // Ищем файл в папке src

        if (!file.exists()) {
            // Если запустили не из корня проекта, пробуем просто index.html
            file = new File("index.html");
        }

        if (file.exists()) {
            // Отправляем код 200 (ОК) и сам файл
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        } else {
            // Файл не найден
            String response = "404 Not Found (Check file path)";
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}