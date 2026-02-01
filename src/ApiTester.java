import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiTester {
    public static void main(String[] args) {
        try {
            System.out.println(">>> TESTING POST REQUEST...");

            // JSON, который мы хотим отправить
            String jsonInput = "{\"text\": \"Question from Java Tester\", \"marks\": 99}";

            // Создаем клиента
            HttpClient client = HttpClient.newHttpClient();

            // Собираем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/questions"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();

            // Отправляем
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Смотрим ответ
            System.out.println("Status Code: " + response.statusCode()); // Должно быть 201
            System.out.println("Response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}