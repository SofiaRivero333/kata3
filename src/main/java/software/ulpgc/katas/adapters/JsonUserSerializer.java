package software.ulpgc.katas.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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

        JsonObject metadata = new JsonObject();
        metadata.addProperty("total", users.size());

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