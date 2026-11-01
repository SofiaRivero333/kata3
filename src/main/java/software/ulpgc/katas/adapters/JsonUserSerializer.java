package software.ulpgc.katas.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import software.ulpgc.katas.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

public class JsonUserSerializer {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(
                    Instant.class,
                    (JsonSerializer<Instant>) (value, type, context) ->
                            new JsonPrimitive(value.toString())
            )
            .create();
}