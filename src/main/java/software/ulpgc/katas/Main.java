package software.ulpgc.katas;

import software.ulpgc.katas.adapters.JsonUserDeserializer;
import software.ulpgc.katas.adapters.JsonUserSerializer;
import software.ulpgc.katas.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build()) {

            String address =
                    "https://gist.githubusercontent.com/THowland1/"
                            + "a527d341bf9d96542a7ddd7574f8fffe/raw/people.json";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(address))
                    .timeout(Duration.ofSeconds(20))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new IOException(
                        "El servidor ha respondido con el codigo "
                                + response.statusCode()
                );
            }

            String json = "[{\"name\":\"Prueba\",\"gender\":\"female\"}]";

            List<User> users =
                    new JsonUserDeserializer().deserialize(json);

            JsonUserSerializer serializer =
                    new JsonUserSerializer();

            serializer.serialize(users, System.out);

            try (OutputStream output =
                         new FileOutputStream("users-output.json")) {
                serializer.serialize(users, output);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error: peticion interrumpida.");
            System.exit(1);

        } catch (IOException | URISyntaxException | RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}