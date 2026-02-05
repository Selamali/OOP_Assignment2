package exam.app; // Добавил package, если он нужен, или удалите если файл в корне src

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiTester {
    public static void main(String[] args) {
        try {
            System.out.println(">>> TESTING POST REQUEST...");

            // ИСПРАВЛЕНО: Добавлена категория в JSON
            String jsonInput = "{\"text\": \"Question from Java Tester\", \"marks\": 99, \"category\": \"API Test\"}";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/questions"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}