package software.ulpgc.katas.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import software.ulpgc.katas.adapters.dtos.UserDto;
import software.ulpgc.katas.model.Gender;
import software.ulpgc.katas.model.User;
import software.ulpgc.katas.ports.UserDeserializer;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class JsonRandomUserDeserializer implements UserDeserializer {
    private final Gson gson = new GsonBuilder()
            .setStrictness(Strictness.STRICT)
            .create();

    @Override
    public List<User> deserialize(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        try {
            JsonObject object = gson.fromJson(json, JsonObject.class);

            if (object == null || !object.has("results")
                    || !object.get("results").isJsonArray()) {
                throw new IllegalArgumentException(
                        "La entrada debe contener un array results."
                );
            }

            return deserialize(object.getAsJsonArray("results"));
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

        String title = user.name.title == null
                ? ""
                : user.name.title.trim();

        return new User(
                String.join(
                        " ",
                        title,
                        user.name.first.trim(),
                        user.name.last.trim()
                ).trim(),
                genderOf(user.gender),
                dateOfBirthOf(user.dob.date)
        );
    }

    private void validate(UserDto user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "el registro esta vacio."
            );
        }

        if (user.name == null || isEmpty(user.name.first)
                || isEmpty(user.name.last)) {
            throw new IllegalArgumentException(
                    "faltan el nombre o los apellidos."
            );
        }

        if (isEmpty(user.gender)) {
            throw new IllegalArgumentException(
                    "falta el genero."
            );
        }

        if (user.dob == null || isEmpty(user.dob.date)) {
            throw new IllegalArgumentException(
                    "falta la fecha de nacimiento."
            );
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isBlank();
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

    private static Instant dateOfBirthOf(String value) {
        try {
            return Instant.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "fecha no valida: " + value, e
            );
        }
    }
}