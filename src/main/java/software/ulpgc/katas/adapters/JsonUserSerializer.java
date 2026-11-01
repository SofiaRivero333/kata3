package software.ulpgc.katas.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import software.ulpgc.katas.model.Gender;
import software.ulpgc.katas.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonUserSerializer {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void serialize(List<User> users, OutputStream output)
            throws IOException {

        long males = users.stream()
                .filter(user -> user.gender() == Gender.male)
                .count();

        long females = users.stream()
                .filter(user -> user.gender() == Gender.female)
                .count();

        JsonObject byGender = new JsonObject();
        byGender.addProperty("male", males);
        byGender.addProperty("female", females);

        JsonObject metadata = new JsonObject();
        metadata.addProperty("total", users.size());
        metadata.add("byGender", byGender);

        JsonObject result = new JsonObject();
        result.add("users", gson.toJsonTree(users));
        result.add("metadata", metadata);

        output.write(
                gson.toJson(result).getBytes(StandardCharsets.UTF_8)
        );

        output.write('\n');
        output.flush();
    }
}