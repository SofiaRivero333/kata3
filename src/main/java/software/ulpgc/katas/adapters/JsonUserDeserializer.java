package software.ulpgc.katas.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import software.ulpgc.katas.adapters.dtos.UserDto;
import software.ulpgc.katas.model.Gender;
import software.ulpgc.katas.model.User;
import software.ulpgc.katas.ports.UserDeserializer;

import java.util.ArrayList;
import java.util.List;

public class JsonUserDeserializer implements UserDeserializer {
    private final Gson gson = new GsonBuilder()
            .setStrictness(Strictness.STRICT)
            .create();

    @Override
    public List<User> deserialize(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        try {
            JsonElement root = gson.fromJson(json, JsonElement.class);

            if (root == null || !root.isJsonArray()) {
                throw new IllegalArgumentException(
                        "La entrada debe ser una lista JSON: [...]."
                );
            }

            return deserialize(root.getAsJsonArray());

        } catch (JsonParseException e) {
            throw new IllegalArgumentException(
                    "JSON invalido: comprueba su formato.", e
            );
        }
    }

    private List<User> deserialize(JsonArray array) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            try {
                users.add(deserialize(array.get(i)));

            } catch (JsonParseException | IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Registro " + (i + 1) + " invalido: "
                                + e.getMessage(),
                        e
                );
            }
        }

        return users;
    }

    private User deserialize(JsonElement item) {
        return deserialize(gson.fromJson(item, UserDto.class));
    }

    private User deserialize(UserDto user) {
        validate(user);

        return new User(
                user.name.trim(),
                genderOf(user.gender.trim()),
                user.age
        );
    }

    private void validate(UserDto user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "el registro esta vacio."
            );
        }

        if (user.name == null || user.name.isBlank()) {
            throw new IllegalArgumentException(
                    "falta el nombre."
            );
        }

        if (user.gender == null || user.gender.isBlank()) {
            throw new IllegalArgumentException(
                    "falta el genero."
            );
        }

        if (user.age == null) {
            throw new IllegalArgumentException(
                    "falta la edad."
            );
        }

        if (user.age < 0) {
            throw new IllegalArgumentException(
                    "la edad no puede ser negativa."
            );
        }
    }

    private static Gender genderOf(String value) {
        try {
            return Gender.valueOf(value);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "genero no valido: " + value, e
            );
        }
    }
}